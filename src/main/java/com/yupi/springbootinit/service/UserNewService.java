package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.entity.UserNew;
import com.yupi.springbootinit.model.vo.LoginUserNewVO;

public interface UserNewService extends IService<UserNew> {

    String sendVerifyCode(String phone);

    void validateVerifyCode(String phone, String verifyId, String verifyCode);

    LoginUserNewVO loginByPhone(String phone);

    LoginUserNewVO loginByPwd(String identifier, String pwd);

    UserNew registerByPhone(String phone);
}
