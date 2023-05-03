package com.bilibili.domain.auth;

import lombok.Data;

import java.util.Date;

// @date 2023/5/2
// @time 16:48
// @author zhangzhi
// @description
@Data
public class AuthRole {
    private Long id;

    private String name;

    private String code;

    private Date createTime;

    private Date updateTime;
}
