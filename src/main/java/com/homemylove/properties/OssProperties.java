package com.homemylove.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oss.aliyun-oss")
public class OssProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String endPoint;
    private String bucketName;
}
