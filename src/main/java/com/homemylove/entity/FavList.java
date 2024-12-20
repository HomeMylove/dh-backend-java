package com.homemylove.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homemylove.core.mp.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName("fav_list")
public class FavList extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    public static LambdaQueryWrapper<FavList> gw(){return new LambdaQueryWrapper<>();}

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String type;
}
