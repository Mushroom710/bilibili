package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/5/4
// @time 11:10
// @author zhangzhi
// @description
@Data
public class VideoTag {

    private Long id;

    private Long videoId;

    private Long tagId;

    private Date createTime;
}
