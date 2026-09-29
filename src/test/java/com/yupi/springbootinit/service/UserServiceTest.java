package com.yupi.springbootinit.service;

import cn.hutool.crypto.SecureUtil;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.model.vo.NewsLoginVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
public class UserServiceTest {

//    @Resource
//    private UserService userService;
    @Resource
    private NewsUserService newsUserService;

//    @Test
//    void userRegister() {
//        String userAccount = "yupi";
//        String userPassword = "";
//        String checkPassword = "123456";
//        try {
//            long result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//            userAccount = "yu";
//            result = userService.userRegister(userAccount, userPassword, checkPassword);
//            Assertions.assertEquals(-1, result);
//        } catch (Exception e) {
//
//        }
//    }

    @Test
    void encPassword() {
        String password = "123456";
        String encodePassword = SecureUtil.hmacSha256(CommonConstant.AUTH_HMAC_KEY).digestHex(password);
        System.out.println(encodePassword);
    }

    @Test
    void loginTest() {
        String identifier = "zhangsan";
        String password = "123456";
        NewsLoginVO  newsLoginVO = newsUserService.login(identifier, password);
        Assertions.assertNotNull(newsLoginVO);
        System.out.println(newsLoginVO);
    }
}
