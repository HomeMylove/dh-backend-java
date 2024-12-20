package com.homemylove.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OssFileConfig {

    private static final Set<String> BIZ_TYPE = new HashSet<>(List.of("avatar"));

    /** 图片类型后缀格式 **/
    private static final Set<String> IMG_SUFFIX = new HashSet<>(Arrays.asList("jpg", "png", "jpeg", "gif"));

    /** 允许上传的最大文件大小的默认值 **/
    private static final Long DEFAULT_MAX_SIZE = 5 * 1024 * 1024L;

    /** 是否在允许的文件类型后缀内 **/
    public static boolean isAllowFileSuffix(String fixSuffix){
        return IMG_SUFFIX.contains(fixSuffix);
    }
    public static boolean isAllowBizType(String bizType){
        return BIZ_TYPE.contains(bizType);
    }

    /** 是否在允许的大小范围内 **/
    public static boolean isMaxSizeLimit(Long fileSize){
        return DEFAULT_MAX_SIZE >= ( fileSize == null ? 0L : fileSize);
    }
}

