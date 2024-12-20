package com.homemylove.auth;

public interface Authenticator {

    /**
     * 认证，传入 token字符串，解析为 AuthInfo对象
     * @param token token 字符串
     * @return AuthInfo 对象
     */
    AuthInfo auth(String token);
}
