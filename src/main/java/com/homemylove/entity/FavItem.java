package com.homemylove.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("fav_item")
public class FavItem extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    public static LambdaQueryWrapper<FavItem> gw(){return new LambdaQueryWrapper<>();}

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("article_id")
    private Long articleId;

    @TableField("fav_id")
    private Long favId;

}
