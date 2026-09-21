package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BookException;
import com.yupi.springbootinit.mapper.BookMapper;
import com.yupi.springbootinit.mapper.BorrowRecordMapper;
import com.yupi.springbootinit.model.dto.book.BookAddRequest;
import com.yupi.springbootinit.model.dto.book.BookBorrowRequest;
import com.yupi.springbootinit.model.dto.book.BookFavoriteRequest;
import com.yupi.springbootinit.model.dto.book.BookQueryRequest;
import com.yupi.springbootinit.model.entity.Book;
import com.yupi.springbootinit.model.entity.BorrowRecord;
import com.yupi.springbootinit.service.BookService;
import com.yupi.springbootinit.utils.MapUtils;
import com.yupi.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private BorrowRecordMapper borrowRecordMapper;


    @Override
    public Page<Book> getBooksByPage(BookQueryRequest bookQueryRequest) {
        return page(
                new Page<>(bookQueryRequest.getCurrent(), bookQueryRequest.getPageSize()),
                getBookQueryWrapper(bookQueryRequest));
    }

    @Override
    public List<Book> getFavoriteBooks(BookFavoriteRequest bookFavoriteRequest) {
        return bookMapper.listFavoriteBook(Objects.requireNonNullElse(bookFavoriteRequest.getTopNum(), 5));
    }

    private QueryWrapper<Book> getBookQueryWrapper(BookQueryRequest bookQueryRequest) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        if (bookQueryRequest == null) {
            return queryWrapper;
        }

        Map<String, Class<?>> fieldMap = Arrays.stream(Book.class.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, Field::getType));

        Map<String, String> fieldReflectMap = Arrays.stream(Book.class.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, field -> {
                    if (field.getAnnotation(TableId.class) != null) {
                      return field.getAnnotation(TableId.class).value();
                    }
                    if(field.getAnnotation(TableField.class) != null) {
                        return field.getAnnotation(TableField.class).value();
                    }
                    return field.getName();
                }));

        if (!MapUtils.isEmpty(bookQueryRequest.getFilterCond())) {
            log.info(bookQueryRequest.getFilterCond().toString());
            bookQueryRequest.getFilterCond().forEach((field, value) -> {
                if (value != null && fieldMap.containsKey(field) && value.getClass().isAssignableFrom(fieldMap.get(field))) {
                    queryWrapper.eq(fieldReflectMap.get(field), value);
                }
            });
        }

        if  (!MapUtils.isEmpty(bookQueryRequest.getSearchCond())) {
            bookQueryRequest.getSearchCond().forEach((field, value) -> {
                if (value != null && fieldMap.containsKey(field) && fieldMap.get(field).equals(String.class)) {
                    queryWrapper.like(fieldReflectMap.get(field), value);
                }
            });
        }

        String sortField = bookQueryRequest.getSortField();
        String sortOrder = bookQueryRequest.getSortOrder();
        if (sortField != null && fieldMap.containsKey(sortField)) {
            queryWrapper.orderBy(
                    SqlUtils.validSortField(sortOrder), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        }

        return  queryWrapper;
    }

    @Override
    public Boolean addBook(BookAddRequest book) {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean borrowBook(BookBorrowRequest borrowRequest) {

        Book book = bookMapper.selectById(borrowRequest.getBookId());
        if (book == null) {
            throw new BookException(ErrorCode.NOT_FOUND_ERROR, "书籍不存在");
        } else if (book.getLeftNum() <= 0) {
            throw  new BookException(ErrorCode.OPERATION_ERROR, "库存不足");
        }

        book.setLeftNum(book.getLeftNum() - 1);
        bookMapper.updateById(book);

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBookId(borrowRequest.getBookId());
        borrowRecord.setUserId(borrowRequest.getUserId());
        borrowRecord.setBorrowTime(new Date());
        borrowRecordMapper.insert(borrowRecord);
        return true;
    }
}
