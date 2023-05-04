package com.bilibili.service;

import com.bilibili.dao.AuthRoleDao;
import com.bilibili.domain.auth.AuthRole;
import com.bilibili.domain.auth.AuthRoleElementOperation;
import com.bilibili.domain.auth.AuthRoleMenu;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

// @date 2023/5/2
// @time 16:59
// @author zhangzhi
// @description
@Service
public class AuthRoleService {

    @Resource
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Resource
    private AuthRoleDao authRoleDao;

    @Resource
    private AuthRoleMenuService authRoleMenuService;

    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationService.getRoleElementOperationsByRoleIds(roleIdSet);
    }

    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuService.getAuthRoleMenusByRoleIds(roleIdSet);
    }

    public AuthRole getRoleByCode(String code) {
        return authRoleDao.getRoleByCode(code);
    }
}
