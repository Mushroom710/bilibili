package com.bilibili.domain.auth;

import lombok.Data;

import java.util.Date;

// @date 2023/5/2
// @time 16:48
// @author zhangzhi
// @description
@Data
public class AuthRoleElementOperation {
    private Long id;

    private Long roleId;

    private Long elementOperationId;

    private Date createTime;

    private AuthElementOperation authElementOperation;
}
