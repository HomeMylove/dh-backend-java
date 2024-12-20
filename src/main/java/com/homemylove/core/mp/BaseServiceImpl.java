package com.homemylove.core.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homemylove.auth.AuthInfo;
import com.homemylove.core.mdc.MDCScope;

import java.util.Date;

public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M,T> {

    @Override
    public boolean save(T entity) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        assert authInfo != null;
        entity.setCreateUser(authInfo.getId());
        entity.setDeleted(0);
        entity.setCreateTime(new Date());
        return super.save(entity);
    }

}
