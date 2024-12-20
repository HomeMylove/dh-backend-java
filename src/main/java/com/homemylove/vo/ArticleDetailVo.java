package com.homemylove.vo;

import lombok.Data;

@Data
public class ArticleDetailVo {
    private Long id;
    private String title;
    private String text;
    private AuthorSearchVo author;
    private CreatorInfo creator;
    @Data
    public static class CreatorInfo{
        private Long id;
        private String name;
    }
}
