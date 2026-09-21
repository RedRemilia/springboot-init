package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@TableName("training.borrow_records")
@Data
public class BorrowRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("book_id")
    private Integer bookId;

    @TableField("user_id")
    private Integer userId;

    @TableField("borrow_time")
    private Date borrowTime;

    @TableField("return_time")
    private Date returnTime;

}
