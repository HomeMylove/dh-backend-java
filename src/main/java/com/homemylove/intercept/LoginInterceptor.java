package com.homemylove.intercept;

import com.homemylove.auth.AuthInfo;
import com.homemylove.auth.Authenticator;
import com.homemylove.core.mdc.MDCScope;
import com.homemylove.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private Authenticator authenticator;

    @Resource
    private RedisTemplate<String,AuthInfo> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String authToken = JwtUtil.getTokenFromRequest(request);
            if(authToken == null){
                MDCScope.removeAuthInfo();
                return true;
            }
            AuthInfo authInfo = redisTemplate.opsForValue().get(authToken);
            if(authInfo != null){
                MDCScope.saveAuthInfo(authInfo);
            }else{
                MDCScope.removeAuthInfo();
            }
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
