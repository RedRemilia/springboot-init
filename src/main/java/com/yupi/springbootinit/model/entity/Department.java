package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@TableName("departments")
@Data
public class Department implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableField("dept_id")
    private Integer departmentId;

    @TableField("dept_name")
    private String departmentName;
}
