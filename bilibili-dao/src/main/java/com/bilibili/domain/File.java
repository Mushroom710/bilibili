package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/5/3
// @time 17:19
// @author zhangzhi
// @description
@Data
public class File {
    private Long id;

    private String url;

    private String type;

    private String md5;

    private Date createTime;
}
