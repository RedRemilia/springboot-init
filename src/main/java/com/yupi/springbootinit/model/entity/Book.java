package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@TableName(value = "training.books")
@Data
public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "book_id", type = IdType.AUTO)
    private Integer bookId;

    @TableField("book_name")
    private String bookName;

    private String author;

    @TableField("total_num")
    private Integer totalNum;

    @TableField("left_num")
    private Integer leftNum;

}


