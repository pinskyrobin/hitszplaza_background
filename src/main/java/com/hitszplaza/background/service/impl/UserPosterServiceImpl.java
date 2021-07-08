package com.hitszplaza.background.service.impl;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.service.UserPosterService;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Service
public class UserPosterServiceImpl implements UserPosterService {

    @Autowired
    private WeChatUtil weChatUtil;

    @Override
    public ArrayList<String> findAll(Integer number) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasequery?access_token=";
        // 数据库查询语句
        String pre_query = "db.collection(\"userPoster\")";
        // 默认每次查询20条
        if (number == null) {
            number = 20;
        }
        int count = weChatUtil.getCount("userPoster");
        int skipCount;
        // 查询轮数
        int batchTimes = (int) Math.ceil((double) count / number);

        ArrayList<String> response = new ArrayList<>();
        String raw_response;

        for (int i = 0; i < batchTimes; i++) {
            skipCount = i * number;
            String query = pre_query + String.format(".skip(%d).limit(%d).get()", skipCount, number);
            raw_response = weChatUtil.post(url, query);
            //TODO:数据结果不够优雅,还需修改!
            response.add(new JSONObject(raw_response).getJSONArray("data").toString());
        }
        return response;
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
    public String findAllWithOpenId(String openId) {
        return null;
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
