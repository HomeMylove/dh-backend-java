package com.homemylove.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.homemylove.auth.AuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ReadHistoryVo {
    private Long id;
    private ArticleInfo article;
    private AuthorInfo author;
    private Integer page;
    private Integer total;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleInfo{
        private Long id;
        private String title;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthorInfo{
        private Long id;
        private String name;
    }
}
