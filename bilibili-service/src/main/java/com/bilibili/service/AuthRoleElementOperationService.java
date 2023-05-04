package com.bilibili.service;

import com.bilibili.dao.AuthRoleElementOperationDao;
import com.bilibili.domain.auth.AuthRoleElementOperation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

// @date 2023/5/2
// @time 17:12
// @author zhangzhi
// @description
@Service
public class AuthRoleElementOperationService {

    @Resource
    private AuthRoleElementOperationDao authRoleElementOperationDao;
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationDao.getRoleElementOperationsByRoleIds(roleIdSet);
    }
}
