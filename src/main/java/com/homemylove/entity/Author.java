package com.homemylove.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("author")
public class Author extends BaseEntity {

    public static LambdaQueryWrapper<Author> gw(){return new LambdaQueryWrapper<>();}

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String kanjiName;

    private String kanaName;

    private String romanName;

    @JsonProperty("about")
    private String about;

}
