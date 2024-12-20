package com.homemylove.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dh.resource.article")
public class ResourceProperties {

    private String path = "/root/dh";

    public String getPath() {
        String basePath = path;
        int lastIndex = basePath.length() - 1;
        String lastWord = basePath.substring(lastIndex);
        if (lastWord.equals("/")) {
            return basePath.substring(0, lastIndex);
        }
        return path;
    }
}
