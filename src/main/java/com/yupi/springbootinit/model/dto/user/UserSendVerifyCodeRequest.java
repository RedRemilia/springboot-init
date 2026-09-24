package com.yupi.springbootinit.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserSendVerifyCodeRequest {

    @NotBlank(message = "请输入手机号")
    @Pattern(
            regexp = "^1[3-9]\\d{9}$",
            message = "请输入正确的手机号"
    )
    private String phone;

}
