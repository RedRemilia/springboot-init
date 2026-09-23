package com.yupi.springbootinit.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserVeriCodeLoginRequest {

    @NotNull
    private String phone;

    @NotNull
    private final String verifyId;

    @NotNull
    private final String verifyCode;

}
