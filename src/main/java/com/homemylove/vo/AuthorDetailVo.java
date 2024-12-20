package com.homemylove.vo;

import lombok.Data;

import java.util.List;

@Data
public class AuthorDetailVo {
    private Long id;
    private String kanjiName;

    private String kanaName;

    private String romanName;

    private String about;

    private List<ArticleSearchVo> articles;
}
