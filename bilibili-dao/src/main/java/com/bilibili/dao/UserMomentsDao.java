package com.bilibili.dao;

import com.bilibili.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

// @date 2023/4/25
// @time 17:13
// @author zhangzhi
// @description

@Mapper
public interface UserMomentsDao {

    Integer addUserMoments(UserMoment userMoment);
}
