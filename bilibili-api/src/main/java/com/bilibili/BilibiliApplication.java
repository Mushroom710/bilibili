package com.bilibili;

import com.bilibili.service.websocket.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

// @date 2023/4/21
// @time 16:59
// @author zhangzhi
// @description
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableWebSocket
public class BilibiliApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(BilibiliApplication.class, args);
        WebSocketService.setApplicationContext(app);
    }
}
