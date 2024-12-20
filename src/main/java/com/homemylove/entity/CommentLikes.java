package com.homemylove.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



@Data
@TableName("comment_likes")
public class CommentLikes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static LambdaQueryWrapper<CommentLikes> gw(){return new LambdaQueryWrapper<>();}

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("comment_id")
    private Long commentId;

    @TableField("create_time")
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;

    @TableLogic
    private Integer deleted;

}