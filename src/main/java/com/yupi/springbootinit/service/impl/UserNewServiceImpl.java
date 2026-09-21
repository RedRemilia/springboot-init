package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.mapper.UserNewMapper;
import com.yupi.springbootinit.model.entity.UserNew;
import com.yupi.springbootinit.service.UserNewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserNewServiceImpl extends ServiceImpl<UserNewMapper, UserNew>
        implements UserNewService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long registerNew(UserNew userNew) {

        this.save(userNew);
        return userNew.getUserId();
    }
}
