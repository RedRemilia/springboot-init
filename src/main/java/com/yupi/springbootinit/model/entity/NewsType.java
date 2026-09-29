package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName news_type
 */
@TableName(value ="news_type")
@Data
public class NewsType implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer tid;

    /**
     * 
     */
    private String tname;

    /**
     * 
     */
    private Integer version;

    /**
     * 
     */
    private Integer idDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}