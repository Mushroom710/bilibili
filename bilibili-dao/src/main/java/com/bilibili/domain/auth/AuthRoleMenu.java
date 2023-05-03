package com.bilibili.domain.auth;

import lombok.Data;

import java.util.Date;

// @date 2023/5/2
// @time 16:49
// @author zhangzhi
// @description
@Data
public class AuthRoleMenu {
    private Long id;

    private Long roleId;

    private Long menuId;

    private Date createTime;

    private AuthMenu authMenu;
}
