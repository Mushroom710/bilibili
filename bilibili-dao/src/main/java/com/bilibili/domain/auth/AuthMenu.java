package com.bilibili.domain.auth;

import lombok.Data;

import java.util.Date;

// @date 2023/5/2
// @time 16:47
// @author zhangzhi
// @description
@Data
public class AuthMenu {
    private Long id;

    private String name;

    private String code;

    private Date createTime;

    private Date updateTime;
}
