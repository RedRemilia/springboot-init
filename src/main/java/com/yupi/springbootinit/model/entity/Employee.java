package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@TableName("employees")
@Data
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    private Integer userId;

    @TableField("user_name")
    private String userName;

    private String password;

    @TableField("created_time")
    private Date createdTime;

    @TableField("last_login_time")
    private Date lastLoginTime;

    private Integer department;

}
