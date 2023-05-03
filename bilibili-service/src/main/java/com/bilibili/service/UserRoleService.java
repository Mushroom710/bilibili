package com.bilibili.service;

import com.bilibili.dao.UserRoleDao;
import com.bilibili.domain.auth.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

// @date 2023/5/2
// @time 16:59
// @author zhangzhi
// @description
@Service
public class UserRoleService {

    @Resource
    private UserRoleDao userRoleDao;

    public List<UserRole> getUserRoleByUserId(Long userId) {
        return userRoleDao.getUserRoleByUserId(userId);
    }

    public void addUserRole(UserRole userRole) {
        userRole.setCreateTime(new Date());
        userRoleDao.addUserRole(userRole);
    }
}
