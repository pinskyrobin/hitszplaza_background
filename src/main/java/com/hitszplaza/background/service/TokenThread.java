package com.hitszplaza.background.service;

import com.hitszplaza.background.pojo.Access;
import com.hitszplaza.background.utils.RedisUtil;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenThread implements Runnable {

    private final WeChatUtil weChatUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    public TokenThread(WeChatUtil weChatUtil) {
        this.weChatUtil = weChatUtil;
    }

    @Override
    @Scheduled(fixedRate = 6000000)
    public void run() {
        Access access = weChatUtil.getToken();
        try {
            // 更新AccessToken
            redisUtil.update("accessToken", access.getAccessToken());
            log.info("获取的accessToken为" + access.getAccessToken());
        } catch (Exception e) {
            try {
                Thread.sleep(30 * 1000);
                // 更新AccessToken
                redisUtil.update("accessToken", access.getAccessToken());
                log.info("重新获取的accessToken为" + access.getAccessToken());
            } catch (InterruptedException e1) {
                log.error("{1}", e1);
            }
            log.error("{1}", e);
        }
    }

}
