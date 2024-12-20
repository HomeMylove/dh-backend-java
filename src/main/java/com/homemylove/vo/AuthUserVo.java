package com.homemylove.vo;

import lombok.Data;

@Data
public class AuthUserVo {
    private Long id;
    private String username;
    private String avatar;
    private String token;
}
