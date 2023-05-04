package com.bilibili.dao;

import com.bilibili.domain.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

// @date 2023/5/2
// @time 17:39
// @author zhangzhi
// @description
@Mapper
public interface AuthRoleMenuDao {
    List<AuthRoleMenu> getAuthRoleMenusByRoleIds(@Param("roleIdSet") Set<Long> roleIdSet);
}
