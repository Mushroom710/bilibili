package com.bilibili.domain;

import lombok.Data;

import java.util.Date;

// @date 2023/4/22
// @time 19:50
// @author zhangzhi
// @description

@Data
public class User {

    private Long id;

    private String phone;

    private String email;

    private String password;

    private String salt;

    private Date createTime;

    private Date updateTime;

    private UserInfo userInfo;
}
