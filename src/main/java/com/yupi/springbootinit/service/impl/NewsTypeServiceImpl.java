package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.NewsType;
import com.yupi.springbootinit.service.NewsTypeService;
import com.yupi.springbootinit.mapper.NewsTypeMapper;
import org.springframework.stereotype.Service;

/**
* @author humingli
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2026-09-29 14:56:15
*/
@Service
public class NewsTypeServiceImpl extends ServiceImpl<NewsTypeMapper, NewsType>
    implements NewsTypeService{

}




