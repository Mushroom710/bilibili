package com.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

// @date 2023/5/5
// @time 11:54
// @author zhangzhi
// @description
@Mapper
public interface UserCoinDao {
    Integer getUserCoinsAmount(Long userId);

    Integer updateUserCoinAmount(@Param("userId") Long userId,
                                 @Param("amount") Integer amount,
                                 @Param("updateTime") Date updateTime);
}
