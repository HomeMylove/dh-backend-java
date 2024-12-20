package com.homemylove.service.impl;

import com.homemylove.auth.AuthInfo;
import com.homemylove.core.cs.CS.REDIS_KEY;
import com.homemylove.core.mdc.MDCScope;
import com.homemylove.service.HistoryService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean saveHistory(REDIS_KEY prefix, String value,Long max) {
        String key = getKey(prefix);
        if(key == null) return false;
        redisTemplate.opsForList().remove(key,0,value);
        redisTemplate.opsForList().leftPush(key,value);
        redisTemplate.opsForList().trim(key,0,max-1);
        return true;
    }

    @Override
    public List<String> getHistory(REDIS_KEY prefix) {
        String key = getKey(prefix);
        if(key == null) return null;
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public boolean deleteHistory(REDIS_KEY prefix, String value) {
        String key = getKey(prefix);
        if(key == null) return false;
        Long remove = redisTemplate.opsForList().remove(key, 1, value);
        return remove > 0;
    }

    private String getKey(REDIS_KEY prefix) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return null;
        return prefix.getKey() + authInfo.getId();
    }
}
