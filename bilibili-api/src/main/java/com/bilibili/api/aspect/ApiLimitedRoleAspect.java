package com.bilibili.api.aspect;

import com.bilibili.api.support.UserSupport;
import com.bilibili.domain.annotation.ApiLimitedRole;
import com.bilibili.domain.auth.UserRole;
import com.bilibili.domain.exception.ConditionException;
import com.bilibili.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// @date 2023/5/2
// @time 20:25
// @author zhangzhi
// @description
@Order(1)
@Component
@Aspect
public class ApiLimitedRoleAspect {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.bilibili.domain.annotation.ApiLimitedRole)")
    public void check(){
    }

    @Before("check() && @annotation(apiLimitedRole)")
    public void deBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole){
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if(roleCodeSet.size() > 0){
            throw new ConditionException("权限不足！");
        }
    }
}
