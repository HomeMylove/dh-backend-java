package com.homemylove.vo;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class FavArticleListVo extends FavListVo{
    private boolean checked;
}
