package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.dto.book.BookAddRequest;
import com.yupi.springbootinit.model.dto.book.BookBorrowRequest;
import com.yupi.springbootinit.model.dto.book.BookFavoriteRequest;
import com.yupi.springbootinit.model.dto.book.BookQueryRequest;
import com.yupi.springbootinit.model.entity.Book;
import com.yupi.springbootinit.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {

    @Resource
    private BookService bookService;

    @PostMapping
    public BaseResponse<Page<Book>> getBooksByPage(@RequestBody BookQueryRequest queryRequest, HttpServletRequest request) {
        return ResultUtils.success(bookService.getBooksByPage(queryRequest));
    }

    @PostMapping("favor")
    public BaseResponse<List<Book>> getFavoriteBook(@RequestBody BookFavoriteRequest bookFavoriteRequest) {
        return ResultUtils.success(bookService.getFavoriteBooks(bookFavoriteRequest));
    }

    @PostMapping("/add")
    public BaseResponse<Boolean> addBook(@RequestBody @Valid BookAddRequest bookAddRequest, HttpServletRequest request) {
        return ResultUtils.success(bookService.addBook(bookAddRequest));
    }

    @PostMapping("/borrow")
    public BaseResponse<Boolean> borrowBook(@RequestBody BookBorrowRequest borrowRequest, HttpServletRequest request) {
        Boolean result = bookService.borrowBook(borrowRequest);
        return ResultUtils.success(result);
    }

}
