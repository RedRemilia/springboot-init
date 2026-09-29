package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.NewsHeadline;
import com.yupi.springbootinit.service.NewsHeadlineService;
import com.yupi.springbootinit.mapper.NewsHeadlineMapper;
import org.springframework.stereotype.Service;

/**
* @author humingli
* @description 针对表【news_headline(文章表)】的数据库操作Service实现
* @createDate 2026-09-29 14:56:15
*/
@Service
public class NewsHeadlineServiceImpl extends ServiceImpl<NewsHeadlineMapper, NewsHeadline>
    implements NewsHeadlineService{

}




