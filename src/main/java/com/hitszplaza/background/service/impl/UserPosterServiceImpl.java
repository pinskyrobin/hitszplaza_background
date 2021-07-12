package com.hitszplaza.background.service.impl;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.service.UserPosterService;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserPosterServiceImpl implements UserPosterService {

    @Autowired
    private WeChatUtil weChatUtil;

    @Override
    public List<Object> findAll(Integer number) {
        // 数据库查询语句
        String preQuery = "db.collection(\"userPoster\")";
        return weChatUtil.queryBatch(number, "userPoster", preQuery);
    }

    @Override
    public String updateOneStatus(String id, Boolean valid) {
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

    @Override
    public String deleteOne(String id) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasedelete?access_token=";
        // 数据库查询语句
        String query = "db.collection(\"userPoster\")"
                .concat(String.format(".doc(\"%s\").remove()", id));
        String response =  weChatUtil.post(url, query);
        JSONObject json = new JSONObject(response);
        if ((Integer) json.get("deleted") == 0) {
            log.warn("未删除任何记录!");
        }
        return response;
    }

    @Override
    public List<Object> findAllWithOpenId(Integer number, String openId) {
        // 数据库查询语句
        String preQuery = String.format("db.collection(\"userPoster\").where({openid:\"%s\"})", openId);
        return weChatUtil.queryBatch(number, "userPoster", preQuery);
    }

    @Override
    public String findAllWithType(String type) {
        return null;
    }

    @Override
    public String findAllWithTime(Date time) {
        return null;
    }

    @Override
    public String findAllWithAnoymous(Integer number) {
        return null;
    }

    //TODO: remain to be completed!
    @Override
    public String updateAllStatusWithOpenId(String openId) {
        return null;
    }


}
