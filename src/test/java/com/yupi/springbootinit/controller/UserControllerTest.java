package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.common.errorcode.AuthErrorCode;
import com.yupi.springbootinit.config.WxOpenConfig;
import com.yupi.springbootinit.exception.AuthException;
import com.yupi.springbootinit.mapper.*;
import com.yupi.springbootinit.model.vo.LoginUserNewVO;
import com.yupi.springbootinit.service.UserNewService;
import com.yupi.springbootinit.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserController 登录接口测试
 *
 * <p>采用 {@link WebMvcTest} 切片,只加载 Web 层(UserController + 全局异常处理器),
 * 三个依赖服务(UserService / UserNewService / WxOpenConfig)全部 mock,
 * 无需连接 MySQL / Redis / Elasticsearch。</p>
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String PHONE = "13800138000";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserNewService userNewService;

    @MockitoBean
    private WxOpenConfig wxOpenConfig;

    /**
     * MainApplication 上的 @MapperScan 会把全部 Mapper 注册为单例并在启动时急切实例化,
     * 而 @WebMvcTest 切片不加载 MyBatis 自动配置(无 SqlSessionFactory),
     * 导致 MapperFactoryBean 初始化报 "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required"。
     * 将全部 Mapper mock 掉,让 BeanOverride 替换其 bean 定义,切片即可正常加载。
     */
    @MockitoBean
    private BookMapper bookMapper;
    @MockitoBean
    private BorrowRecordMapper borrowRecordMapper;
    @MockitoBean
    private PostFavourMapper postFavourMapper;
    @MockitoBean
    private PostMapper postMapper;
    @MockitoBean
    private PostThumbMapper postThumbMapper;
    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private UserNewMapper userNewMapper;

    private LoginUserNewVO buildLoginUserNewVO() {
        LoginUserNewVO vo = new LoginUserNewVO();
        vo.setUserId(1);
        vo.setUserName("testUser");
        vo.setToken("jwt-token");
        return vo;
    }

    // region 发送验证码 /user/login/send_code

    @Test
    @DisplayName("发送验证码成功")
    void sendVerifyCode_success() throws Exception {
        when(userNewService.sendVerifyCode(PHONE)).thenReturn("verifyId-abc");

        mockMvc.perform(post("/user/login/send_code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + PHONE + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").value("verifyId-abc"));
    }

    @Test
    @DisplayName("发送验证码-非手机号格式校验失败")
    void sendVerifyCode_invalidPhone_shouldFailValidation() throws Exception {
        mockMvc.perform(post("/user/login/send_code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(40000))
                .andExpect(jsonPath("$.message").value(containsString("phone字段有误")));
    }

    @Test
    @DisplayName("发送验证码-冷却中返回业务错误码")
    void sendVerifyCode_inCooldown_shouldReturnAuthErrorCode() throws Exception {
        when(userNewService.sendVerifyCode(PHONE))
                .thenThrow(new AuthException(AuthErrorCode.VERIFY_SEND_COOLDOWN));

        mockMvc.perform(post("/user/login/send_code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + PHONE + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AuthErrorCode.VERIFY_SEND_COOLDOWN.getCode()))
                .andExpect(jsonPath("$.message").value(containsString("冷却")));
    }

    // endregion

    // region 验证码登录 /user/login/veri_code

    @Test
    @DisplayName("验证码登录成功-返回用户信息和token")
    void veriCodeLogin_success() throws Exception {
        when(userNewService.loginByPhone(PHONE)).thenReturn(buildLoginUserNewVO());

        mockMvc.perform(post("/user/login/veri_code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + PHONE + "\",\"verifyId\":\"vid-1\",\"verifyCode\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.validate.status").value(true))
                .andExpect(jsonPath("$.data.validate.message").value("success"))
                .andExpect(jsonPath("$.data.loginUserNew.userId").value(1))
                .andExpect(jsonPath("$.data.loginUserNew.token").value("jwt-token"));

        verify(userNewService).validateVerifyCode(PHONE, "vid-1", "123456");
        verify(userNewService).loginByPhone(PHONE);
    }

    @Test
    @DisplayName("验证码登录-验证码错误返回status=false")
    void veriCodeLogin_wrongCode_shouldReturnValidateFalse() throws Exception {
        doThrow(new AuthException(AuthErrorCode.VERIFY_FAILED))
                .when(userNewService).validateVerifyCode(PHONE, "vid-1", "000000");

        mockMvc.perform(post("/user/login/veri_code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + PHONE + "\",\"verifyId\":\"vid-1\",\"verifyCode\":\"000000\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.validate.status").value(false))
                .andExpect(jsonPath("$.data.validate.message").value(AuthErrorCode.VERIFY_FAILED.getMessage()))
                .andExpect(jsonPath("$.data.loginUserNew").doesNotExist());

        verify(userNewService, never()).loginByPhone(anyString());
    }

    @Test
    @DisplayName("验证码登录-超过尝试次数返回status=false")
    void veriCodeLogin_exceedAttempts_shouldReturnValidateFalse() throws Exception {
        doThrow(new AuthException(AuthErrorCode.VERIFY_FAIL_EXCEEDED))
                .when(userNewService).validateVerifyCode(PHONE, "vid-1", "000000");

        mockMvc.perform(post("/user/login/veri_code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + PHONE + "\",\"verifyId\":\"vid-1\",\"verifyCode\":\"000000\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.validate.status").value(false))
                .andExpect(jsonPath("$.data.validate.message").value(AuthErrorCode.VERIFY_FAIL_EXCEEDED.getMessage()));
    }

    @Test
    @DisplayName("验证码登录-手机号非手机号格式校验失败")
    void veriCodeLogin_invalidPhone_shouldFailValidation() throws Exception {
        mockMvc.perform(post("/user/login/veri_code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"123\",\"verifyId\":\"vid-1\",\"verifyCode\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(40000))
                .andExpect(jsonPath("$.message").value(containsString("phone字段有误")));
    }

    @Test
    @DisplayName("验证码登录-验证码为空校验失败")
    void veriCodeLogin_blankVerifyCode_shouldFailValidation() throws Exception {
        mockMvc.perform(post("/user/login/veri_code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"" + PHONE + "\",\"verifyId\":\"vid-1\",\"verifyCode\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(40000))
                .andExpect(jsonPath("$.message").value(containsString("verifyCode字段有误")));
    }

    // endregion

    // region 密码登录 /user/login/pwd

    @Test
    @DisplayName("密码登录成功-邮箱账号")
    void pwdLogin_success_withEmail() throws Exception {
        when(userNewService.loginByPwd("user@example.com", "pwd123")).thenReturn(buildLoginUserNewVO());

        mockMvc.perform(post("/user/login/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"user@example.com\",\"password\":\"pwd123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.validate.status").value(true))
                .andExpect(jsonPath("$.data.loginUserNew.token").value("jwt-token"));

        verify(userNewService).loginByPwd("user@example.com", "pwd123");
    }

    @Test
    @DisplayName("密码登录成功-手机号账号")
    void pwdLogin_success_withPhone() throws Exception {
        when(userNewService.loginByPwd(PHONE, "pwd123")).thenReturn(buildLoginUserNewVO());

        mockMvc.perform(post("/user/login/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"" + PHONE + "\",\"password\":\"pwd123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.validate.status").value(true))
                .andExpect(jsonPath("$.data.loginUserNew.token").value("jwt-token"));
    }

    @Test
    @DisplayName("密码登录-密码错误返回status=false")
    void pwdLogin_wrongPassword_shouldReturnValidateFalse() throws Exception {
        doThrow(new AuthException(AuthErrorCode.WRONG_PASSWORD))
                .when(userNewService).loginByPwd(PHONE, "wrong");

        mockMvc.perform(post("/user/login/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"" + PHONE + "\",\"password\":\"wrong\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.validate.status").value(false))
                .andExpect(jsonPath("$.data.loginUserNew").doesNotExist());
    }

    @Test
    @DisplayName("密码登录-用户不存在返回status=false")
    void pwdLogin_userNotExist_shouldReturnValidateFalse() throws Exception {
        doThrow(new AuthException(AuthErrorCode.USER_NOT_EXIST))
                .when(userNewService).loginByPwd("nobody@example.com", "pwd123");

        mockMvc.perform(post("/user/login/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"nobody@example.com\",\"password\":\"pwd123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.validate.status").value(false));
    }

    @Test
    @DisplayName("密码登录-账号非手机号/邮箱格式校验失败")
    void pwdLogin_invalidIdentifier_shouldFailValidation() throws Exception {
        mockMvc.perform(post("/user/login/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"abc\",\"password\":\"pwd123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(40000))
                .andExpect(jsonPath("$.message").value(containsString("identifier字段有误")));
    }

    @Test
    @DisplayName("密码登录-密码为空校验失败")
    void pwdLogin_blankPassword_shouldFailValidation() throws Exception {
        mockMvc.perform(post("/user/login/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"identifier\":\"" + PHONE + "\",\"password\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(40000))
                .andExpect(jsonPath("$.message").value(containsString("password字段有误")));
    }

    // endregion
}