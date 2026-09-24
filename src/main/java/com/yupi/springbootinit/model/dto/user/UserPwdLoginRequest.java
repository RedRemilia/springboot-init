package com.yupi.springbootinit.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserPwdLoginRequest implements Serializable
{
    @Serial
	private static final long serialVersionUID = 1L;

    @NotBlank(message = "账号不能为空")
    @Pattern(
        regexp = "^(1[3-9]\\d{9}|[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$",
        message = "账号必须是手机号或邮箱"
    )
    private String identifier;

    @NotBlank(message = "密码不能为空")
    private String password;
}
