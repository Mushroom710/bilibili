package com.bilibili.api;

import com.bilibili.api.support.UserSupport;
import com.bilibili.domain.JsonResponse;
import com.bilibili.domain.auth.UserAuthorities;
import com.bilibili.service.UserAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

// @date 2023/5/2
// @time 16:54
// @author zhangzhi
// @description 用户权限控制
@RestController
public class UserAuthApi {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserAuthService userAuthService;

    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthorities> getUserAuthorities(){
        Long userId = userSupport.getCurrentUserId();
        UserAuthorities userAuthorities = userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }
}
