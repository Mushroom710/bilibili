package com.bilibili.dao;

import com.bilibili.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

// @date 2023/5/3
// @time 9:32
// @author zhangzhi
// @description
@Mapper
public interface AuthRoleDao {
    AuthRole getRoleByCode(String code);
}
