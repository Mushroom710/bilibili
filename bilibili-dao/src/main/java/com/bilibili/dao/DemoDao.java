package com.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;

// @date 2023/4/21
// @time 17:55
// @author zhangzhi
// @description
@Mapper
public interface DemoDao {

    public String query(Long id);
}
