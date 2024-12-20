package com.homemylove.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class ArticleFavVo extends ArticleSearchVo{

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Long favId;
}
