package com.homemylove.vo;

import lombok.Data;

@Data
public class ArticleSearchVo {
    private Long id;
    private String title;
    private AuthorSearchVo author;
    private CreatorInfo creator;
    private int deleted;
    @Data
    public static class CreatorInfo{
        private Long id;
        private String name;
    }
}
