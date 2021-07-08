package com.hitszplaza.background.service.impl;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.exception.WeChatException;
import com.hitszplaza.background.service.UserInfoService;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private WeChatUtil weChatUtil;

    @Override
    public String findAll(Integer number) throws WeChatException {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasequery?access_token=";
        // 数据库查询语句
        String query = "db.collection(\"userInfo\")";
        query = (number != null) ?
                query.concat(String.format(".limit(%d).get()", number)) :
                query.concat(".get()");

        // 发送POST查询请求
        return weChatUtil.post(url, query);
    }
}
