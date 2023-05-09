package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/5/8
// @time 16:11
// @author zhangzhi
// @description
@Data
public class Barrage {
    private Long id;

    private Long userId;

    private Long videoId;

    private String content;

    private String barrageTime;

    private Date createTime;
}
