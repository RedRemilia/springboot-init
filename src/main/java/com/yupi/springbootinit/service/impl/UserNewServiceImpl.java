package com.yupi.springbootinit.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.errorcode.AuthErrorCode;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.constant.RedisKeys;
import com.yupi.springbootinit.exception.AuthException;
import com.yupi.springbootinit.mapper.UserNewMapper;
import com.yupi.springbootinit.model.entity.UserNew;
import com.yupi.springbootinit.model.vo.LoginUserNewVO;
import com.yupi.springbootinit.service.UserNewService;
import com.yupi.springbootinit.utils.RedisUtil;
import com.yupi.springbootinit.utils.CustomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserNewServiceImpl extends ServiceImpl<UserNewMapper, UserNew>
        implements UserNewService {

    private final RedisUtil redisUtil;
    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> verifyCodeScript;

    /**
     * 发送验证码
     * @param phone 手机号
     * @return 验证ID
     */
    @Override
    public String sendVerifyCode(String phone) {
        // 1. 验证当前手机号是否处于申请验证码冷却期间
        String phoneLimitKey = RedisKeys.AUTH_LOGIN_LIMIT_PHONE.formatted(phone);
        if (!redisUtil.setIfAbsent(phoneLimitKey, 1, 60)) {
            throw new AuthException(AuthErrorCode.VERIFY_SEND_COOLDOWN);
        }
        // 2. 生成验证ID
        String verifyId = CustomUtils.generateRandomStr(12);
        // 3. 构造验证信息json，包括手机号、重试次数、加密验证码
        Map<String, Object> verifyInfo = new HashMap<>();
        verifyInfo.put("phone", phone);
        verifyInfo.put("attempts", 0);
        String verifyCode = CustomUtils.generateRandomStr(6, true);
        log.debug("测试验证码:{}", verifyCode);
        verifyInfo.put("code", SecureUtil.hmacSha256(CommonConstant.AUTH_HMAC_KEY).digestHex(verifyCode));
        //4. 存储到redis，设置300秒过期
        String verifyInfoKey = RedisKeys.AUTH_LOGIN_VERIFY_ID.formatted(verifyId);
        redisUtil.hmset(verifyInfoKey, verifyInfo);
        redisUtil.expire(verifyInfoKey, 300);
        // 5. 返回验证ID
        return verifyId;
    }

    /**
     * 校验验证码
     * @param phone 手机号
     * @param verifyId 验证ID
     * @param verifyCode 验证码
     */
    @Override
    public void validateVerifyCode(String phone, String verifyId, String verifyCode) {
        String verifyInfoKey = RedisKeys.AUTH_LOGIN_VERIFY_ID.formatted(verifyId);
        String inputVerifyCode = SecureUtil.hmacSha256(CommonConstant.AUTH_HMAC_KEY).digestHex(verifyCode);

        Integer MAX_ATTEMPTS = 5;
        Long result = stringRedisTemplate.execute(
                verifyCodeScript,
                List.of(verifyInfoKey),
                phone,
                inputVerifyCode,
                String.valueOf(MAX_ATTEMPTS)
        );
        switch (result.intValue()) {
            case 1 -> log.debug("验证成功");
            case -1 -> throw new AuthException(AuthErrorCode.VERIFY_CODE_EXPIRE);
            case -2 -> throw new AuthException(AuthErrorCode.VERIFY_PHONE_CHANGED);
            case -3 -> throw new AuthException(AuthErrorCode.VERIFY_FAIL_EXCEEDED);
            default -> throw new  AuthException(AuthErrorCode.VERIFY_FAILED);
        }

    }

    /**
     * 根据手机号获取用户信息，没有自动注册
     *
     * @param phone 手机号
     * @return 用户信息视图
     */
    @Override
    public LoginUserNewVO loginByPhone(String phone) {
        QueryWrapper<UserNew> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserNew userNew = getOne(queryWrapper);
        if (userNew == null) {
            userNew = registerByPhone(phone);
        } else {
            userNew.setLastLoginTime(LocalDateTime.now());
            updateById(userNew);
        }
        return buildLoginUserInfo(userNew);
    }

    @Override
    public LoginUserNewVO loginByPwd(String identifier, String pwd) {
        // 根据phone或者email查找用户，两列均有唯一索引
        LambdaQueryWrapper<UserNew> queryWrapper = new QueryWrapper<UserNew>().lambda();
        queryWrapper.eq(UserNew::getPhone, identifier)
                .or().eq(UserNew::getEmail, identifier);
        UserNew userNew = getOne(queryWrapper);
        if (userNew == null) {
            throw new AuthException(AuthErrorCode.USER_NOT_EXIST);
        }
        String encPwd = SecureUtil.hmacSha256(CommonConstant.AUTH_HMAC_KEY).digestHex(pwd);
        if (!encPwd.equals(userNew.getPassword())) {
            throw new AuthException(AuthErrorCode.WRONG_PASSWORD);
        }
        return buildLoginUserInfo(userNew);
    }

    /**
     * 使用手机号注册
     * @param phone 手机号
     * @return 1
     */
    @Override
    public UserNew registerByPhone(String phone) {
        UserNew userNew = new UserNew();
        userNew.setPhone(phone);
        userNew.setUserName(CustomUtils.generateRandomStr(6));
        userNew.setCreateTime(LocalDateTime.now());
        userNew.setUpdateTime(LocalDateTime.now());
        userNew.setLastLoginTime(LocalDateTime.now());
        save(userNew);
        return userNew;
    }


    /**
     * 将用户信息转换成前端所需格式
     * @param userNew 用户表用户信息
     * @return 前端所需用户信息
     */
    public LoginUserNewVO buildLoginUserInfo(UserNew userNew) {
        LoginUserNewVO loginUserNewVO = new LoginUserNewVO();
        loginUserNewVO.setUserId(userNew.getUserId());
        loginUserNewVO.setUserName(userNew.getUserName());
        // 创建token
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("userId", userNew.getUserId());
//        payload.put(JWTPayload.ISSUED_AT, System.currentTimeMillis()/1000);
//        payload.put(JWTPayload.EXPIRES_AT, System.currentTimeMillis()/1000 + 3600);
//        payload.put(JWTPayload.NOT_BEFORE, System.currentTimeMillis()/1000);
//        String token = JWTUtil.createToken(payload, CommonConstant.GENERATE_TOKEN_KEY.getBytes());

        String token = JWT.create()
                .setPayload("userId", userNew.getUserId())
                .setPayload("phone", userNew.getPhone())
                .setKey(CommonConstant.GENERATE_TOKEN_KEY.getBytes(StandardCharsets.UTF_8))
                .setExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .sign();
        loginUserNewVO.setToken(token);

        return loginUserNewVO;
    }

}
