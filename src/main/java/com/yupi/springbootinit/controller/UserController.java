package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.annotation.Auth;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.common.errorcode.HttpErrorCode;
import com.yupi.springbootinit.exception.AuthException;
import com.yupi.springbootinit.exception.BaseException;
import com.yupi.springbootinit.model.dto.user.UserPwdLoginRequest;
import com.yupi.springbootinit.model.dto.user.UserSendVerifyCodeRequest;
import com.yupi.springbootinit.model.dto.user.UserVeriCodeLoginRequest;
import com.yupi.springbootinit.model.vo.LoginVO;
import com.yupi.springbootinit.model.vo.NewsLoginVO;
import com.yupi.springbootinit.model.vo.ValidateVO;
import com.yupi.springbootinit.service.NewsUserService;
import com.yupi.springbootinit.service.UserNewService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserNewService userNewService;

    @Resource
    private NewsUserService newsUserService;

    // region 登录相关
    /**
     * 发送验证码
     */
    @PostMapping("/login/send_code")
    public BaseResponse<String> sendVerifyCode(@RequestBody @Valid UserSendVerifyCodeRequest userSendVerifyCodeRequest) {

        if (userSendVerifyCodeRequest == null) {
            throw new BaseException(HttpErrorCode.PARAMS_ERROR);
        }
        String verifyId = userNewService.sendVerifyCode(userSendVerifyCodeRequest.getPhone());
        return ResultUtils.success(verifyId);
    }

    /**
     * 验证码登录/注册
     */
    @PostMapping("/login/veri_code")
    public BaseResponse<LoginVO> veriCodeLogin(@RequestBody @Valid UserVeriCodeLoginRequest userVeriCodeLoginRequest) {
        if (userVeriCodeLoginRequest == null) {
            throw new BaseException(HttpErrorCode.PARAMS_ERROR);
        }
        String phone = userVeriCodeLoginRequest.getPhone();
        String verifyId = userVeriCodeLoginRequest.getVerifyId();
        String verifyCode = userVeriCodeLoginRequest.getVerifyCode();
        LoginVO loginVO = new LoginVO();
        ValidateVO validate = new ValidateVO();
        loginVO.setValidate(validate);
        try {
            // 校验验证码
            userNewService.validateVerifyCode(phone, verifyId, verifyCode);
            validate.setStatus(true);
            validate.setMessage("success");
            // 获取用户信息
             loginVO.setLoginUserNew(userNewService.loginByPhone(phone));
            return ResultUtils.success(loginVO);
        } catch (AuthException e) {
            // 验证失败返回false
            validate.setStatus(false);
            validate.setMessage(e.getMessage());
            return ResultUtils.success(loginVO);
        }

    }

    @PostMapping("/login/pwd")
    public BaseResponse<NewsLoginVO> pwdLogin(@RequestBody @Valid UserPwdLoginRequest userPwdLoginRequest) {
        if (userPwdLoginRequest == null) {
            throw new BaseException(HttpErrorCode.PARAMS_ERROR);
        }
        String identifier = userPwdLoginRequest.getIdentifier();
        String password = userPwdLoginRequest.getPassword();
        return ResultUtils.success(newsUserService.login(identifier, password));
    }

    @PostMapping("/update")
    @Auth
    public BaseResponse<Boolean> updateUser(@RequestBody UserPwdLoginRequest userPwdLoginRequest) {
        if (userPwdLoginRequest == null) {
            throw new BaseException(HttpErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(true);
    }

//    /**
//     * 用户登录
//     */
//    @PostMapping("/login/pwd")
//    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
//        if (userLoginRequest == null) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        String userAccount = userLoginRequest.getUserAccount();
//        String userPassword = userLoginRequest.getUserPassword();
//        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
//        return ResultUtils.success(loginUserVO);
//    }

//    /**
//     * 用户登录（微信开放平台）
//     */
//    @GetMapping("/login/wx_open")
//    public BaseResponse<LoginUserVO> userLoginByWxOpen(HttpServletRequest request,
//                                                       @RequestParam("code") String code) {
//        WxOAuth2AccessToken accessToken;
//        try {
//            WxMpService wxService = wxOpenConfig.getWxMpService();
//            accessToken = wxService.getOAuth2Service().getAccessToken(code);
//            WxOAuth2UserInfo userInfo = wxService.getOAuth2Service().getUserInfo(accessToken, code);
//            String unionId = userInfo.getUnionId();
//            String mpOpenId = userInfo.getOpenid();
//            if (StringUtils.isAnyBlank(unionId, mpOpenId)) {
//                throw new BaseException(OldErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
//            }
//            return ResultUtils.success(userService.userLoginByMpOpen(userInfo, request));
//        } catch (Exception e) {
//            log.error("userLoginByWxOpen error", e);
//            throw new BaseException(OldErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
//        }
//    }

//    /**
//     * 用户注销
//     */
//    @PostMapping("/logout")
//    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
//        if (request == null) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        boolean result = userService.userLogout(request);
//        return ResultUtils.success(result);
//    }

//    /**
//     * 获取当前登录用户
//     */
//    @GetMapping("/get/login")
//    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
//        User user = userService.getLoginUser(request);
//        return ResultUtils.success(userService.getLoginUserVO(user));
//    }

    // endregion

    // region 增删改查

//    /**
//     * 创建用户
//     */
//    @PostMapping("/add")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
//        if (userAddRequest == null) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        User user = new User();
//        BeanUtils.copyProperties(userAddRequest, user);
//        boolean result = userService.save(user);
//        ThrowUtils.throwIf(!result, OldErrorCode.OPERATION_ERROR);
//        return ResultUtils.success(user.getId());
//    }

//    /**
//     * 删除用户
//     */
//    @PostMapping("/delete")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
//        if (deleteRequest == null || deleteRequest.getId() <= 0) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        boolean b = userService.removeById(deleteRequest.getId());
//        return ResultUtils.success(b);
//    }
//
//    /**
//     * 更新用户
//     */
//    @PostMapping("/update")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
//        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        User user = new User();
//        BeanUtils.copyProperties(userUpdateRequest, user);
//        boolean result = userService.updateById(user);
//        ThrowUtils.throwIf(!result, OldErrorCode.OPERATION_ERROR);
//        return ResultUtils.success(true);
//    }

//    /**
//     * 根据 id 获取用户（仅管理员）
//     */
//    @GetMapping("/get")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<User> getUserById(long id) {
//        if (id <= 0) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        User user = userService.getById(id);
//        ThrowUtils.throwIf(user == null, OldErrorCode.NOT_FOUND_ERROR);
//        return ResultUtils.success(user);
//    }
//
//    /**
//     * 根据 id 获取包装类
//     */
//    @GetMapping("/get/vo")
//    public BaseResponse<UserVO> getUserVOById(long id) {
//        BaseResponse<User> response = getUserById(id);
//        User user = response.getData();
//        return ResultUtils.success(userService.getUserVO(user));
//    }
//
//    /**
//     * 分页获取用户列表（仅管理员）
//     */
//    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
//        long current = userQueryRequest.getCurrent();
//        long size = userQueryRequest.getPageSize();
//        Page<User> userPage = userService.page(new Page<>(current, size),
//                userService.getQueryWrapper(userQueryRequest));
//        return ResultUtils.success(userPage);
//    }
//
//    /**
//     * 分页获取用户封装列表
//     */
//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
//        if (userQueryRequest == null) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        long current = userQueryRequest.getCurrent();
//        long size = userQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, OldErrorCode.PARAMS_ERROR);
//        Page<User> userPage = userService.page(new Page<>(current, size),
//                userService.getQueryWrapper(userQueryRequest));
//        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
//        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
//        userVOPage.setRecords(userVO);
//        return ResultUtils.success(userVOPage);
//    }
//
//    // endregion
//
//    /**
//     * 更新个人信息
//     */
//    @PostMapping("/update/my")
//    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
//            HttpServletRequest request) {
//        if (userUpdateMyRequest == null) {
//            throw new BaseException(OldErrorCode.PARAMS_ERROR);
//        }
//        User loginUser = userService.getLoginUser(request);
//        User user = new User();
//        BeanUtils.copyProperties(userUpdateMyRequest, user);
//        user.setId(loginUser.getId());
//        boolean result = userService.updateById(user);
//        ThrowUtils.throwIf(!result, OldErrorCode.OPERATION_ERROR);
//        return ResultUtils.success(true);
//    }
}
