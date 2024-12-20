package com.homemylove.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public static LambdaQueryWrapper<User> gw(){return new LambdaQueryWrapper<>();}

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String account;

    private String password;

    private Integer gender;  // 0 female   1 male

    private String avatar;

    private String signature;

    private String salt;

    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;

    @TableLogic
    private Integer deleted;

}
