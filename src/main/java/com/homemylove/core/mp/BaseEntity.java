package com.homemylove.core.mp;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    private Long createUser;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    @TableLogic
    private Integer deleted;

}

