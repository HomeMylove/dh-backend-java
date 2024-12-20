package com.homemylove.utils;

import com.homemylove.auth.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    // 生成Token
    public static String generateToken(AuthInfo authInfo, long expirationTime, String secret) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", authInfo.getId());
        claims.put("username", authInfo.getUsername());
        claims.put("avatar",authInfo.getAvatar());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    // 验证Token并解析为对象
    public static AuthInfo verifyToken(String token, String secret) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("过期了");
        }
        AuthInfo authInfo = new AuthInfo();
        authInfo.setId(claims.get("id", Long.class));
        authInfo.setUsername(claims.get("username", String.class));
        authInfo.setAvatar(claims.get("avatar",String.class));
        return authInfo;
    }


    public static String getTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader("token");
        token = StringUtils.isEmpty(token) ? request.getHeader(HttpHeaders.AUTHORIZATION) : token;
        if(StringUtils.isEmpty(token)){
            return null;
        }
        int index = token.indexOf(" ");
        String authToken = "";
        if(index == -1){
            authToken = token;
        }else {
            String tokenType = token.substring(0,index);
            if(!"Bearer".equals(tokenType)){
                throw new RuntimeException(String.format("无法识别的的token类型[%s]",tokenType));
            }else {
                authToken = token.substring(index).trim();
            }
        }
        return authToken;
    }
}

