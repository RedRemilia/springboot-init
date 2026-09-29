package com.yupi.springbootinit.model.vo;

import lombok.Data;

@Data
public class NewsLoginVO {

    private Long userId;
    private String userName;
    private String nickName;
    private String token;

}
