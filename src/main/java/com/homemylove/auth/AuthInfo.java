package com.homemylove.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthInfo implements Serializable {
    private Long id;
    private String username;
    private String avatar;
}
