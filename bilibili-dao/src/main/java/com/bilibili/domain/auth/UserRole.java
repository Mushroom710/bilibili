package com.bilibili.domain.auth;

import lombok.Data;

import java.util.Date;

// @date 2023/5/2
// @time 16:50
// @author zhangzhi
// @description
@Data
public class UserRole {
    private Long id;

    private Long userId;

    private Long roleId;

    private String roleName;

    private String roleCode;

    private Date createTime;
}
