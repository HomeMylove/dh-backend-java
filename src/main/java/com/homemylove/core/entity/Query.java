package com.homemylove.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query {
    private Integer pageNum;
    private Integer pageSize;
}
