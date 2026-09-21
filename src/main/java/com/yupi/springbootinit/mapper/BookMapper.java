package com.yupi.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.springbootinit.model.entity.Book;

import java.util.List;

public interface BookMapper extends BaseMapper<Book> {

    List<Book> listFavoriteBook(Integer topNum);

}
