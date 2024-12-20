package com.homemylove.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommenterVo {
    private Long id;
    private String name;
    private String avatar;
}
