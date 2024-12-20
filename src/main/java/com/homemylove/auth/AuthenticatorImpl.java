package com.homemylove.auth;

import com.homemylove.properties.JwtProperties;
import com.homemylove.utils.JwtUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AuthenticatorImpl implements Authenticator {

    @Resource
    private JwtProperties jwtProperties;

    @Override
    public AuthInfo auth(String token) {
        String authToken;
        int index = token.indexOf(" ");
        if (index == -1) {
            authToken = token;
        } else {
            String tokenType = token.substring(0, index);
            if (!"Bearer".equals(tokenType)) {
                throw new RuntimeException(String.format("无法识别的的token类型[%s]", tokenType));
            } else {
                authToken = token.substring(index).trim();
            }
        }
        return JwtUtil.verifyToken(authToken, jwtProperties.getSecret());
    }
}
