package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName(value = "userNew", autoResultMap = true)
public class UserNew implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    Integer userId;

    String userName;

    String password;

    @TableField(typeHandler = JacksonTypeHandler.class)
    Address homeAddr;

    @TableField(typeHandler = JacksonTypeHandler.class)
    Address workAddr;

    Boolean isDeleted;

}
