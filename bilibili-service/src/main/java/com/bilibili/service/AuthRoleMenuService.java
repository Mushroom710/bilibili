package com.bilibili.service;

import com.bilibili.dao.AuthRoleMenuDao;
import com.bilibili.domain.auth.AuthRoleMenu;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

// @date 2023/5/2
// @time 17:13
// @author zhangzhi
// @description
@Service
public class AuthRoleMenuService {

    @Resource
    private AuthRoleMenuDao authRoleMenuDao;

    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuDao.getAuthRoleMenusByRoleIds(roleIdSet);
    }
}
