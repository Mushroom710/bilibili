package com.bilibili.dao;

import com.bilibili.domain.Barrage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

// @date 2023/5/8
// @time 16:12
// @author zhangzhi
// @description
@Mapper
public interface BarrageDao {
    Integer addBarrage(Barrage barrage);

    List<Barrage> getBarrage(Map<String, Object> params);
}
