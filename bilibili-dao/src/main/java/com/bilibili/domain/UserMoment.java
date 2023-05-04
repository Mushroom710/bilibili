package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/4/25
// @time 17:13
// @author zhangzhi
// @description

@Data
public class UserMoment {

    private Long id;

    private Long userId;

    private String type;

    private Long contentId;

    private Date createTime;

    private Date updateTime;
}
