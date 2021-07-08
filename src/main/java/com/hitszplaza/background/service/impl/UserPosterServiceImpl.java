package com.hitszplaza.background.service.impl;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.service.UserPosterService;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserPosterServiceImpl implements UserPosterService {

    @Autowired
    private WeChatUtil weChatUtil;

    @Override
    public String findAll(Integer number) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasequery?access_token=";
        // 数据库查询语句
        String query = "db.collection(\"userPoster\")";
        query = (number != null) ?
                query.concat(String.format(".limit(%d).get()", number)) :
                query.concat(".get()");

        // 发送POST查询请求
        return weChatUtil.post(url, query);
    }

    @Override
    public String updateStatus(String id, Boolean valid) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        String tmp_valid = valid ? "true" : "false";
        // 数据库查询语句
        String query = "db.collection(\"userPoster\")"
                .concat(String.format(".doc(\"%s\")" +
                        ".update({data:{valid: %s}})", id, tmp_valid));
        String response =  weChatUtil.post(url, query);
        JSONObject json = new JSONObject(response);
        if ((Integer) json.get("matched") == 0) {
            log.warn("未匹配到任何记录");
        } else if ((Integer) json.get("modified") == 0) {
            log.warn("未修改任何记录");
        }
        return response;
    }


}
