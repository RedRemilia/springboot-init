package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章表
 * @TableName news_headline
 */
@TableName(value ="news_headline")
@Data
public class NewsHeadline implements Serializable {
    /**
     * 文章ID
     */
    @TableId(type = IdType.AUTO)
    private Integer hid;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 文章类型
     */
    private Integer type;

    /**
     * 发布者ID
     */
    private Integer publisher;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 最近修改时间
     */
    private Date updateTime;

    /**
     * 是否已被删除
     */
    private Integer idDeleted;

    /**
     * 乐观锁
     */
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}