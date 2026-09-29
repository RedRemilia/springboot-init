package com.yupi.springbootinit.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.errorcode.AuthErrorCode;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.AuthException;
import com.yupi.springbootinit.model.entity.NewsUser;
import com.yupi.springbootinit.model.vo.NewsLoginVO;
import com.yupi.springbootinit.service.NewsUserService;
import com.yupi.springbootinit.mapper.NewsUserMapper;
import com.yupi.springbootinit.utils.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author humingli
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2026-09-29 14:56:15
*/
@Service
@RequiredArgsConstructor
public class NewsUserServiceImpl extends ServiceImpl<NewsUserMapper, NewsUser>
    implements NewsUserService{

    private final JwtHelper jwtHelper;

    @Override
    public NewsLoginVO login(String identifier, String password) {
        LambdaQueryWrapper<NewsUser> queryWrapper = new LambdaQueryWrapper<NewsUser>();
        queryWrapper.eq(NewsUser::getUsername, identifier);
        NewsUser newsUser = getOne(queryWrapper);
        if(newsUser == null){
            throw new AuthException(AuthErrorCode.USER_NOT_EXIST);
        }

        String encodePassword = SecureUtil.hmacSha256(CommonConstant.AUTH_HMAC_KEY).digestHex(password);
        if (!encodePassword.equals(newsUser.getPassword())) {
            throw new AuthException(AuthErrorCode.WRONG_PASSWORD);
        }

        String token = jwtHelper.createToken(newsUser.getUid());
        NewsLoginVO newsLoginVO = new NewsLoginVO();
        newsLoginVO.setUserId(newsUser.getUid());
        newsLoginVO.setUserName(newsUser.getUsername());
        newsLoginVO.setNickName(newsUser.getNickName());
        newsLoginVO.setToken(token);
        return newsLoginVO;
    }
}




