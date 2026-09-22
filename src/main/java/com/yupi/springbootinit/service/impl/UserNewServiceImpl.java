package com.yupi.springbootinit.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.errorcode.AuthErrorCode;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.constant.RedisKeys;
import com.yupi.springbootinit.exception.AuthException;
import com.yupi.springbootinit.mapper.UserNewMapper;
import com.yupi.springbootinit.model.entity.UserNew;
import com.yupi.springbootinit.service.UserNewService;
import com.yupi.springbootinit.utils.RedisUtils;
import com.yupi.springbootinit.utils.CustomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserNewServiceImpl extends ServiceImpl<UserNewMapper, UserNew>
        implements UserNewService {


    private final RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long registerNew(UserNew userNew) {

        this.save(userNew);
        return userNew.getUserId();
    }

    @Override
    public String sendVerifyCode(String phone) {
        // 1. 验证当前手机号是否处于申请验证码冷却期间
        String phoneLimitKey = RedisKeys.AUTH_LOGIN_LIMIT_PHONE.formatted(phone);
        if (!redisUtils.setIfAbsent(phoneLimitKey, 1, 60)) {
            throw new AuthException(AuthErrorCode.VERIFY_SEND_COOLDOWN);
        }
        // 2. 生成验证ID
        String verifyId = CustomUtils.generateRandomStr(12);
        // 3. 构造验证信息json，包括手机号、重试次数、加密验证码
        Map<String, Object> verifyInfo = new HashMap<>();
        verifyInfo.put("phone", phone);
        verifyInfo.put("attempts", 0);
        String verifyCode = CustomUtils.generateRandomStr(6, true);
        log.info("测试验证码:{}", verifyCode);
        verifyInfo.put("code", SecureUtil.hmacSha256(CommonConstant.AUTH_HMAC_KEY).digestHex(verifyCode));
        //4. 存储到redis，设置300秒过期
        String verifyInfoKey = RedisKeys.AUTH_LOGIN_VERIFY_ID.formatted(verifyId);
        redisUtils.hmset(verifyInfoKey, verifyInfo);
        redisUtils.expire(verifyInfoKey, 300);
        // 5. 返回验证ID
        return verifyId;
    }

    @Override
    public void validateVerifyCode(String verifyId, String verifyCode) {
        // 1. 判断verifyId是否存在
        String verifyInfoKey = RedisKeys.AUTH_LOGIN_VERIFY_ID.formatted(verifyId);
        Boolean exist = redisUtils.exist(verifyInfoKey);
        if (!exist) {
            throw new AuthException(AuthErrorCode.VERIFY_CODE_EXPIRE);
        }
        // 2. 判断验证码是否正确
        String encVerifyCode = redisUtils.hget(verifyInfoKey, "code").toString();
        String inputVerifyCode = SecureUtil.hmacSha256(CommonConstant.AUTH_HMAC_KEY).digestHex(verifyCode);

        Long attempts;
        if (!encVerifyCode.equals(inputVerifyCode)) {
            attempts = redisUtils.hIncrBy(verifyInfoKey, "attempts");
            if (attempts >= 5) {
                redisUtils.del(verifyInfoKey);
            }
            throw new AuthException(AuthErrorCode.VERIFY_CODE_EXPIRE);
        }

        attempts = (Long) redisUtils.hget(verifyInfoKey, "attempts");
        if (attempts >= 5) {

        }


    }
}
