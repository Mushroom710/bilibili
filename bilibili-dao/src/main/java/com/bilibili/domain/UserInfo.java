package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/4/22
// @time 20:08
// @author zhangzhi
// @description

@Data
public class UserInfo {

    private Long id;

    private Long userId;

    private String nick;

    private String avatar;

    private String sign;

    private String gender;

    private String birth;

    private Date createTime;

    private Date updateTime;

    private Boolean followed;
}
