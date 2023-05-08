package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/5/5
// @time 10:36
// @author zhangzhi
// @description
@Data
public class VideoCollection {
    private Long id;

    private Long videoId;

    private Long userId;

    private Long groupId;

    private Date createTime;
}
