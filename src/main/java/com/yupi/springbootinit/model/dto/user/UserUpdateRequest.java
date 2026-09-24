package com.yupi.springbootinit.model.dto.user;

import java.io.Serial;
import java.io.Serializable;

public class UserUpdateRequest implements Serializable {

    @Serial
    private static final  long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;

}
