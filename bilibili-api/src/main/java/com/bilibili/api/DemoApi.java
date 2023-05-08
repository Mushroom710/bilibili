package com.bilibili.api;

import com.bilibili.service.DemoService;
import com.bilibili.service.util.FastDFSUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

// @date 2023/4/21
// @time 18:03
// @author zhangzhi
// @description
@RestController
public class DemoApi {

    @Resource
    private DemoService demoService;

    @Resource
    private FastDFSUtil fastDFSUtil;

    @GetMapping("/query")
    public String query(Long id) {
        return demoService.query(id);
    }

    @GetMapping("/slices")
    public void slices(MultipartFile file) throws Exception {
        fastDFSUtil.convertFileToSlices(file);
    }
}
