package com.bilibili.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

// @date 2023/5/5
// @time 16:42
// @author zhangzhi
// @description
@Data
public class VideoComment {
    private Long id;

    private Long videoId;

    private Long userId;

    private String comment;

    private Long replyUserId;

    private Long rootId;

    private Date createTime;

    private Date updateTime;

    private List<VideoComment> childList;

    private UserInfo userInfo;

    private UserInfo replyUserInfo;
}
