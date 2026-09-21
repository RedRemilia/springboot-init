package com.yupi.springbootinit.model.vo;

import lombok.Data;

@Data
public class FavoriteBookVO {

    private Integer bookId;

    private String bookName;

    private String author;

    private Integer totalBorrowNum;
}
