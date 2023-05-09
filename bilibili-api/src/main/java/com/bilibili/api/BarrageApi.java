package com.bilibili.api;

import com.bilibili.api.support.UserSupport;
import com.bilibili.domain.Barrage;
import com.bilibili.domain.JsonResponse;
import com.bilibili.service.BarrageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

// @date 2023/5/8
// @time 17:54
// @author zhangzhi
// @description
@RestController
public class BarrageApi {

    @Resource
    private BarrageService barrageService;

    @Resource
    private UserSupport userSupport;

    @GetMapping("/barrages")
    public JsonResponse<List<Barrage>> getBarrages(@RequestParam Long videoId,
                                                   String startTime,
                                                   String endTime) throws Exception {
        List<Barrage> list;
        try {
            // 判断当前是游客模式还是用户登录模式
            userSupport.getCurrentUserId();
            // 若是用户登录模式，则允许用户进行时间段筛选
            list = barrageService.getBarrages(videoId, startTime, endTime);
        } catch (Exception ignored) {
            // 若为游客模式，则不允许用户进行时间段筛选
            list = barrageService.getBarrages(videoId, null, null);
        }

        return new JsonResponse<>(list);
    }
}
