package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

// @date 2023/4/24
// @time 18:02
// @author zhangzhi
// @description

@Data
@AllArgsConstructor
public class PageResult<T> {

    private Integer total;

    private List<T> list;
}
