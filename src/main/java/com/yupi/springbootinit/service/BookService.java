package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.dto.book.BookAddRequest;
import com.yupi.springbootinit.model.dto.book.BookBorrowRequest;
import com.yupi.springbootinit.model.dto.book.BookFavoriteRequest;
import com.yupi.springbootinit.model.dto.book.BookQueryRequest;
import com.yupi.springbootinit.model.entity.Book;

import java.util.List;

public interface BookService extends IService<Book> {

    Page<Book> getBooksByPage(BookQueryRequest request);

    List<Book> getFavoriteBooks(BookFavoriteRequest bookFavoriteRequest);

    Boolean addBook(BookAddRequest book);

    Boolean borrowBook(BookBorrowRequest borrowRequest);
}
