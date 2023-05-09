package com.bilibili.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.domain.Barrage;
import com.bilibili.domain.constant.BarrageConstant;
import com.bilibili.service.BarrageService;
import com.bilibili.service.util.RocketMQUtil;
import com.bilibili.service.util.TokenUtil;
import io.netty.util.internal.StringUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// @date 2023/5/8
// @time 15:32
// @author zhangzhi
// @description 功能还不完善 - 还需要进一步的改进
@Component
@ServerEndpoint("/webserver/{token}")
public class WebSocketService {

    // 保存每一个客户端对应的 WebSocketService -- ConcurrentHashMap保证线程安全
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();
    // 统计在线人数 -- AtomicInteger 可以保证线程安全
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    // 通过 ApplicationContext 解决多例模式下 Bean 的注入问题
    // 具体的就是通过 ApplicationContext 的 getBean() 方法获取指定的 Bean
    private static ApplicationContext APPLICATION_CONTEXT;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // 服务端和客户端通信的会话
    private Session session;
    // 客户端唯一标识
    private String sessionId;
    private Long userId;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 创建WebSocket连接时触发
     */
    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token) {
        try {
            this.userId = TokenUtil.verifyToken(token);
        } catch (Exception ignored) {
        }
        this.sessionId = session.getId();
        this.session = session;
        if (WEBSOCKET_MAP.containsKey(sessionId)) {
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId, this);
        } else {
            WEBSOCKET_MAP.put(sessionId, this);
            ONLINE_COUNT.getAndIncrement();
        }
        logger.info("用户连接成功：" + sessionId + "，当前在线人数为：" + ONLINE_COUNT.get());
        try {
            this.sendMessage("200");
        } catch (Exception e) {
            logger.error("连接异常");
        }
    }

    /**
     * 关闭WebSocket连接时触发
     */
    @OnClose
    public void closeConnection() {
        if (WEBSOCKET_MAP.containsKey(sessionId)) {
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("用户退出：" + sessionId + "，当前在线人数为：" + ONLINE_COUNT.get());
    }

    /**
     * 接收到消息时触发
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("用户信息：" + sessionId + "，报文：" + message);
        if (!StringUtil.isNullOrEmpty(message)) {
            try {
                // 群发消息 -- 采用消息队列提高并发
                for (Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()) {
                    WebSocketService webSocketService = entry.getValue();
                    DefaultMQProducer barrageProducer = (DefaultMQProducer) APPLICATION_CONTEXT.getBean("barrageProducer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", message);
                    jsonObject.put("sessionId", webSocketService.getSessionId());
                    Message msg = new Message(BarrageConstant.TOPIC_BARRAGES, jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
                    RocketMQUtil.asyncSendMsg(barrageProducer, msg);
                }
                if (this.userId != null) {
                    // 保存弹幕到数据库
                    Barrage barrage = JSONObject.parseObject(message, Barrage.class);
                    barrage.setUserId(userId);
                    barrage.setCreateTime(new Date());
                    BarrageService barrageService = (BarrageService) APPLICATION_CONTEXT.getBean("barrageService");
                    // 异步保存弹幕
                    barrageService.asyncAddBarrage(barrage);
                    // 保存弹幕到 redis
                    barrageService.addBarrageToRedis(barrage);
                }
            } catch (Exception e) {
                logger.error("弹幕接收异常");
                e.printStackTrace();
            }
        }
    }

    /**
     * WebSocket连接异常时触发
     */
    @OnError
    public void onError(Session session, Throwable error) {
        try {
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.error("WebSocket连接发生异常，message：" + error.getMessage());
    }

    /**
     * 采用定时任务的方式定时推送在线人数给前端
     */
    @Scheduled(fixedRate = 5000)
    public void noticeOnLineCount() throws IOException {
        for (Map.Entry<String, WebSocketService> entry : WebSocketService.WEBSOCKET_MAP.entrySet()) {
            WebSocketService webSocketService = entry.getValue();
            if (webSocketService.session.isOpen()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount", ONLINE_COUNT.get());
                jsonObject.put("msg", "当前在线人数为" + ONLINE_COUNT.get());
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public Session getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }
}