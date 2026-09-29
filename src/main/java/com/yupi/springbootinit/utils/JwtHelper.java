package com.yupi.springbootinit.utils;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtHelper {


    private String sign; // 签名key

    private long expiration; // 有效时间, 单位ms

    /**
     * 根据UserId分发token
     * @param userId
     * @return
     */
    public String createToken(Long userId) {
        return JWT.create()
                .setPayload("userId", userId)
                .setKey(sign.getBytes(StandardCharsets.UTF_8))
                .setExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign();
    }

    /**
     * 从token中解析UserId
     * @param token
     * @return
     */
    public Long getUserIdFromToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return ((NumberWithFormat) jwt.getPayload("userId")).longValue();
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        try {
            JWTValidator jwtValidator = JWTValidator.of(token);
            byte[] signBytes = sign.getBytes(StandardCharsets.UTF_8);
            // 校验key
            jwtValidator.validateAlgorithm(JWTSignerUtil.hs256(signBytes));
            // 校验过期时间
            jwtValidator.validateDate(new Date());
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
