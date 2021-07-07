package com.hitszplaza.background.service.impl;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.service.UserInfoService;
import com.hitszplaza.background.utils.RedisUtil;
import com.hitszplaza.background.utils.WeChatUtil;
import exception.WeChatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private WeChatUtil weChatUtil;

    @Autowired
    private RedisUtil redisUtil;

    private String accessToken;

    public void updateAccessToken() {
        this.accessToken = redisUtil.get("accessToken");
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public String findAll(Integer number) throws WeChatException {
        // 更新 AccessToken
        updateAccessToken();
        // 返回值
        String response = null;
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasequery?access_token=" + getAccessToken();
        // 数据库查询语句
        String query = "db.collection(\"userInfo\")";
        query = (number != null) ?
                query.concat(String.format(".limit(%d).get()", number)) :
                query.concat(".get()");

        // 发送POST查询请求
        return weChatUtil.post(url, query);
    }
}
