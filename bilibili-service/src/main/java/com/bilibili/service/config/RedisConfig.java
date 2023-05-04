package com.bilibili.service.config;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// @date 2023/5/3
// @time 18:18
// @author zhangzhi
// @description

@Configuration
public class RedisConfig implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if(RedisTemplate.class.isAssignableFrom(bean.getClass())) {
            RedisTemplate redisTemplate = (RedisTemplate)bean;
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(stringRedisSerializer);
        }
        return bean;
    }
}
