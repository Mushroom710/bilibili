package com.bilibili.service;

import com.bilibili.dao.DemoDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

// @date 2023/4/21
// @time 18:01
// @author zhangzhi
// @description
@Service
public class DemoService {

    @Resource
    private DemoDao demoDao;

    public String query(Long id){
        return demoDao.query(id);
    }
}
