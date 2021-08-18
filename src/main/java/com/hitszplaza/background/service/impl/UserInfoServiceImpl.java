package com.hitszplaza.background.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.constant.WeChatDataBaseQueryConstant;
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

    /***
     * @description 获取指定用户的所有信息
     * @param openId 用户标识_openid
     * @return List/<Object/>形式的jsonArray数组,每一个元素为查询所得记录
     */

    @Override
    public JsonObject find(String openId) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasequery?access_token=";
        // 数据库查询语句
        String query = String.format("db.collection(\"userInfo\").where({_openid:\"%s\"}).get()", openId);
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /***
     * @description 获取用户的所有信息
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     * @return List/<Object/>形式的jsonArray数组,每一个元素为查询所得记录
     */
    @Override
    public JsonObject findAll(Integer pageNo, Integer pageSize) {
        // 数据库查询语句
        String preQuery = "db.collection(\"userInfo\")";
        String response = weChatUtil.pageQuery(pageNo, pageSize, preQuery);
        return new Gson().fromJson(response, JsonObject.class);
    }

    @Override
    public JsonObject findByCondition(Integer pageNo, Integer pageSize, String match) {
        String query =  String.format(WeChatDataBaseQueryConstant.USER_INFO_FIND_BY_OPERATION,
                match, pageNo, pageSize);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    @Override
    public JsonObject updateStatus(String openId, Boolean valid) {
        return null;
    }


}
