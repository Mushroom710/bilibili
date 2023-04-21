package com.bilibili;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

// @date 2023/4/21
// @time 16:59
// @author zhangzhi
// @description
@SpringBootApplication
public class BilibiliApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(BilibiliApplication.class, args);
    }
}
