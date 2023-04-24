package com.bilibili.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

// @date 2023/4/23
// @time 21:09
// @author zhangzhi
// @description
@Data
public class FollowingGroup {
    private Long id;

    private Long userId;

    private String name;

    private String type;

    private Date createTime;

    private Date updateTime;

    private List<UserInfo> followingUserInfoList;

}
