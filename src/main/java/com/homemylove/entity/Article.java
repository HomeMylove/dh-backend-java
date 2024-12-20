package com.homemylove.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.homemylove.core.mp.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;


@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName("article")
public class Article extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    public static LambdaQueryWrapper<Article> gw(){return new LambdaQueryWrapper<>();}

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("path")
    private String path;

    @TableField("author_id")
    @JsonProperty("authorId")
    private Long authorId;
}

