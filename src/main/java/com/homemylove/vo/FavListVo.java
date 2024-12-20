package com.homemylove.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavListVo {
    private Long id;
    private String name;
    private String type;
    private Integer total;
    private Integer size;

}
