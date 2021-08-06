package com.hitszplaza.background.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {


    @Autowired
    private RedisUtil redisUtil;

    public RedisKeyExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        super(redisMessageListenerContainer);
    }

    /***
     * @description 针对 redis 数据失效事件，进行数据处理
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 拿到key
        log.info("监听Redis key过期，key：{}，channel：{}", message.toString(), new String(pattern));
        int index = message.toString().lastIndexOf("_");
        String key = "swiper" + message.toString().substring(index);
        redisUtil. hashSetField(key, "currStatus", "1");
    }
}
