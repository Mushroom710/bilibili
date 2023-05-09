package com.bilibili.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.dao.BarrageDao;
import com.bilibili.domain.Barrage;
import io.netty.util.internal.StringUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

// @date 2023/5/8
// @time 16:20
// @author zhangzhi
// @description
@Service
public class BarrageService {
    private static final String BARRAGE_KEY = "dm-video-";

    @Resource
    private BarrageDao barrageDao;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public void addBarrage(Barrage barrage) {
        barrageDao.addBarrage(barrage);
    }

    @Async
    public void asyncAddBarrage(Barrage barrage) {
        barrageDao.addBarrage(barrage);
    }

    /**
     * 查询策略是优先查redis中的弹幕数据，
     * 如果没有的话查询数据库，然后把查询的数据写入redis当中
     */
    public List<Barrage> getBarrages(Long videoId, String startTime, String endTime) throws Exception {
        String key = BARRAGE_KEY + videoId;
        String value = redisTemplate.opsForValue().get(key);
        List<Barrage> list;
        if (!StringUtil.isNullOrEmpty(value)) {
            list = JSONArray.parseArray(value, Barrage.class);
            if (!StringUtil.isNullOrEmpty(startTime) && !StringUtil.isNullOrEmpty(endTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                List<Barrage> childList = new ArrayList<>();
                for (Barrage barrage : list) {
                    Date createTime = barrage.getCreateTime();
                    if (createTime.after(startDate) && createTime.before(endDate)) {
                        childList.add(barrage);
                    }
                }
                list = childList;
            }
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("videoId", videoId);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            list = barrageDao.getBarrage(params);
            // 保存弹幕到 redis
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
        }
        return list;
    }

    public void addBarrageToRedis(Barrage barrage) {
        String key = BARRAGE_KEY + barrage.getVideoId();
        String value = redisTemplate.opsForValue().get(key);
        List<Barrage> list = new ArrayList<>();
        if (!StringUtil.isNullOrEmpty(value)) {
            list = JSONArray.parseArray(value, Barrage.class);
        }
        list.add(barrage);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
    }
}
