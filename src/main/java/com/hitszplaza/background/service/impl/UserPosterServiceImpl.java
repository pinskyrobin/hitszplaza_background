package com.hitszplaza.background.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.constant.WeChatDataBaseQueryConstant;
import com.hitszplaza.background.service.UserPosterService;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserPosterServiceImpl implements UserPosterService {

    @Autowired
    private WeChatUtil weChatUtil;

    /***
     * @description 根据 _id 获取帖子
     */
    @Override
    public JsonObject find(String id) {
        String query = String.format(WeChatDataBaseQueryConstant.USER_POSTER_FIND_BY_ID, id);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /***
     * @description 获取所有userPoster
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     */
    @Override
    public JsonObject findAll(Integer pageNo, Integer pageSize) {
        String query = String.format(WeChatDataBaseQueryConstant.USER_POSTER_FIND_ALL, pageNo, pageSize);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }


    @Override
    public JsonObject findByCondition(Integer pageNo, Integer pageSize, String match) {
        String query =  String.format(WeChatDataBaseQueryConstant.USER_POSTER_FIND_BY_OPERATION,
                match, pageNo, pageSize);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }



    /***
     * @description 更新某个userPoster状态
     * @param id 主键_id
     * @param valid 待更新的valid字段,true = 有效,false = 无效
     * @return 包含errcode, errmsg, matched, modified和id的response返回体,
     *          matched为更新条件匹配到的结果数.
     *          modified为修改的记录数，注意：使用set操作新插入的数据不计入修改数目.
     *          id为新插入记录的id，注意：只有使用set操作新插入数据时这个字段会有值.
     */
    @Override
    public JsonObject updateStatus(Integer mode, String id, Boolean valid) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        String tmpValid = valid ? "true" : "false";
        String query = null;
        // 模式1 修改某个 userPoster
        // 模式2 修改某个 openId 的 userPoster
        if (mode == 1) {
            query = "db.collection(\"userPoster\")"
                    .concat(String.format(".doc(\"%s\")" +
                            ".update({data:{valid: %s}})", id, tmpValid));
        } else if (mode == 2) {
            query = "db.collection(\"userPoster\")"
                    .concat(String.format(".where({openid: \"%s\"})" +
                            ".update({data:{valid: %s}})", id, tmpValid));
        } else {
            log.error("更新 userPoster 时遇到未知模式！");
        }
        String response = weChatUtil.post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("matched").getAsInt() == 0) {
            log.warn("未匹配到任何记录");
        } else if (json.get("modified").getAsInt() == 0) {
            log.warn("匹配到记录，但未修改任何记录");
        }
        return json;
    }

    /***
     * @description 删除某个userPoster
     * @param id 主键_id
     * @return 包含errcode, errmsg和deleted的response返回体.
     *          deleted为删除记录数量.
     */
    @Override
    public JsonObject deleteOne(String id) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasedelete?access_token=";
        // 数据库查询语句
        String query = "db.collection(\"userPoster\")"
                .concat(String.format(".doc(\"%s\").remove()", id));
        String response = weChatUtil.post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("deleted").getAsInt() == 0) {
            log.warn("未删除任何记录!");
        }
        return json;
    }
}
