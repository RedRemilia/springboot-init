package com.yupi.springbootinit.utils;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JwtHelperTest {

    @Resource
    JwtHelper jwtHelper;

    @Test
    void JwtHelperTest_1() {
        String token = jwtHelper.createToken(1L);
        assertNotNull(token);
        Boolean valid = jwtHelper.validateToken(token);
        assert(valid);
        Long userId = jwtHelper.getUserIdFromToken(token);
        assert(userId == 1L);
    }

}