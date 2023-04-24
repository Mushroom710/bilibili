package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/4/23
// @time 21:08
// @author zhangzhi
// @description
@Data
public class UserFollowing {
    private Long id;

    private Long userId;

    private Long followingId;

    private Long groupId;

    private Date createTime;

    private UserInfo userInfo;
}
