package com.bilibili.service;

import com.bilibili.dao.UserCoinDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

// @date 2023/5/5
// @time 11:51
// @author zhangzhi
// @description
@Service
public class UserCoinService {

    @Resource
    private UserCoinDao userCoinDao;

    public Integer getUserCoinsAmount(Long userId) {
        return userCoinDao.getUserCoinsAmount(userId);
    }

    public void updateUserCoinsAmount(Long userId, Integer amount) {
        Date updateTime = new Date();
        userCoinDao.updateUserCoinAmount(userId, amount, updateTime);
    }
}
