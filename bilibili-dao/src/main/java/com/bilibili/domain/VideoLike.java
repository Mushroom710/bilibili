package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/5/5
// @time 10:06
// @author zhangzhi
// @description
@Data
public class VideoLike {
    private Long id;

    private Long userId;

    private Long videoId;

    private Date createTime;
}
