package com.yupi.springbootinit.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserValidateVerifyCodeRequest {

    @NotNull
    private final String verifyId;

    @NotNull
    private final String verifyCode;

}
