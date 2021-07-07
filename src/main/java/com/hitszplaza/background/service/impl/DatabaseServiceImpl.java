package com.hitszplaza.background.service.impl;


//import com.hitszplaza.background.constant.WeChatAPIConstant;
//import com.hitszplaza.background.service.DatabaseService;
//import com.hitszplaza.background.utils.RedisUtil;
//import com.hitszplaza.background.utils.WeChatUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//
//@Slf4j
//@Service
//public class DatabaseServiceImpl implements DatabaseService {
//
//    @Autowired
//    private WeChatUtil weChatUtil;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    private final String accessToken = redisUtil.get("accessToken");
//
//    public String getAccessToken() {
//        return this.accessToken;
//    }
//
//    @Override
//    public String findOne() {
//        return null;
//    }
//
//    @Override
//    public ArrayList<String> findAll() {
//        return null;
//    }
//
//    @Override
//    public void updateOne() {
//        String url = WeChatAPIConstant.WX_API_HOST +
//                "/tcb/updateindex?access_token=" + getAccessToken();
//        JSONObject json = new JSONObject();
//        restTemplate.postForEntity(url, weChatUtil.wrapHttpParams(json), String.class);
//    }
//
//    @Override
//    public void deleteOne() {
//
//    }
//}
