package com.yupi.springbootinit.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSendVerifyCodeRequest {

    @NotNull
    private String phone;

}
