package com.homemylove.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleCreateVo {
    private Long id;
    private String title;

    private AuthorInfo author;
    private CreatorInfo creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Data
    public static class AuthorInfo{
        private Long id;
        private String name;
    }

    @Data
    public static class CreatorInfo{
        private Long id;
        private String name;
    }


}
