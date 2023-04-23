package com.bilibili.dao;

import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

// @date 2023/4/22
// @time 20:13
// @author zhangzhi
// @description

@Mapper
public interface UserDao {
    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    void updateUsers(User user);

    void updateUserInfos(UserInfo userInfo);
}
