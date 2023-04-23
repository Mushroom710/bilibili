package com.bilibili.api;

import com.bilibili.api.support.UserSupport;
import com.bilibili.domain.JsonResponse;
import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import com.bilibili.service.UserService;
import com.bilibili.service.util.RSAUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

// @date 2023/4/22
// @time 20:12
// @author zhangzhi
// @description

@RestController
public class UserApi {
    @Resource
    private UserService userService;

    @Resource
    private UserSupport userSupport;


    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        Long userId = userSupport.getCurrentUserId();
        User user =  userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }

    // 获取 RSA 公钥
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey(){
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    // 用户注册
    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user){
        userService.addUser(user);
        // 为什么这里可以直接返回 success
        // 因为可能失败的情况已经在 UserService 中检查过了，如果出错，就会直接抛异常，然后就会被
        // 全局异常捕获到，进行相关的处理，所以能走到这里，说明前面的流程是没有问题的。
        return JsonResponse.success();
    }

    // 用户登录
    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    // 更新用户账号信息
    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return JsonResponse.success();
    }

    // 更新用户信息
    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo){
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }
}
