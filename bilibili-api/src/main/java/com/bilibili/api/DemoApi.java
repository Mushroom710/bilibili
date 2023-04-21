package com.bilibili.api;

import com.bilibili.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

// @date 2023/4/21
// @time 18:03
// @author zhangzhi
// @description
@RestController
public class DemoApi {

    @Resource
    private DemoService demoService;

    @GetMapping("/query")
    public String query(Long id){
        return demoService.query(id);
    }
}
