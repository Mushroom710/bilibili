package com.bilibili.domain.auth;

import lombok.Data;

import java.util.List;

// @date 2023/5/2
// @time 16:49
// @author zhangzhi
// @description
@Data
public class UserAuthorities {
    List<AuthRoleElementOperation> roleElementOperationList;

    List<AuthRoleMenu> roleMenuList;
}
