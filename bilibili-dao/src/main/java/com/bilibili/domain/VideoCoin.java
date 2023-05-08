package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/5/5
// @time 11:29
// @author zhangzhi
// @description
@Data
public class VideoCoin {
    private Long id;

    private Long videoId;

    private Long userId;

    private Integer amount;

    private Date createTime;

    private Date updateTime;
}
