package com.hitszplaza.background.service;

import com.hitszplaza.background.pojo.Access;
import com.hitszplaza.background.utils.RedisUtil;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void run() {
        while (true) {
            try {
                Access access = weChatUtil.getToken();
                if (access != null) {
                    if (access.getAccessToken() != null) {
                        // 更新AccessToken
                        redisUtil.update("accessToken", access.getAccessToken());
                        log.info("获取的accessToken为" + access.getAccessToken());
                    }
                }
                assert access != null;
                if (access.getAccessToken() != null) {
                    Thread.sleep(access.getExpireTime() * 1000);
                } else {
                    // 如果access_token为null，30秒后再获取
                    Thread.sleep(30 * 1000);
                }
            } catch (InterruptedException e) {
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e1) {
                    log.error("{}", e1);
                }
                log.error("{}", e);
            }
        }
    }
}
