package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.entity.UserNew;

public interface UserNewService extends IService<UserNew> {

    long registerNew(UserNew userNew);

}
