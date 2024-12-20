package com.homemylove.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt.auth")
public class JwtProperties {
    private String secret;
    private long expiration;
}
