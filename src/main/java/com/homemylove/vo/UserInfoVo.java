package com.homemylove.vo;

import lombok.Data;

@Data
public class UserInfoVo {
    private Long id;
    private String username;
    private String avatar;
    private String signature;
    private Integer gender;
    private String account;
}
