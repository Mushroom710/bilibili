package com.bilibili.domain.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

// @date 2023/5/2
// @time 20:22
// @author zhangzhi
// @description 数据权限控制
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface DataLimited {
}
