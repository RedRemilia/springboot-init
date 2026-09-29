package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.entity.NewsUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.vo.LoginVO;
import com.yupi.springbootinit.model.vo.NewsLoginVO;

/**
* @author humingli
* @description 针对表【news_user】的数据库操作Service
* @createDate 2026-09-29 14:56:15
*/
public interface NewsUserService extends IService<NewsUser> {

    public NewsLoginVO login(String identifier, String password);

}
