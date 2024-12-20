package com.homemylove.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UploadAuthorHistoryVo extends UploadHistoryVo{
    private String name;
}
