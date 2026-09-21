package com.yupi.springbootinit.model.dto.book;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BookBorrowRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer bookId;
    private Integer userId;

}
