package com.homemylove.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString(callSuper = true)
@TableName("read_history")
public class ReadHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static LambdaQueryWrapper<ReadHistory> gw(){return new LambdaQueryWrapper<>();}

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("article_id")
    private Long articleId;

    @TableField("user_id")
    private Long userId;

    private Integer page;

    private Integer total;

    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField("create_time")
    private Date createTime;

    @TableLogic
    private Integer deleted;
}
