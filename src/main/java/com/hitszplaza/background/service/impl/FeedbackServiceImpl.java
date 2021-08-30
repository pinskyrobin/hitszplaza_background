package com.hitszplaza.background.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.constant.WeChatDataBaseQueryConstant;
import com.hitszplaza.background.service.FeedbackService;
import com.hitszplaza.background.service.UserInfoService;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private WeChatUtil weChatUtil;

    /***
     * @description 获取特定反馈信息
     */

    @Override
    public JsonObject find(String id) {
        String query = String.format(WeChatDataBaseQueryConstant.FEEDBACK_FIND_BY_ID, id);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /***
     * @description 获取用户的所有信息
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     */
    @Override
    public JsonObject findAll(Integer pageNo, Integer pageSize) {
        String query = String.format(WeChatDataBaseQueryConstant.FEEDBACK_FIND_ALL, pageNo, pageSize);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /***
     * @description 条件查找
     * @param match 筛选语句
     */
    @Override
    public JsonObject findByCondition(Integer pageNo, Integer pageSize, String match) {
        String query =  String.format(WeChatDataBaseQueryConstant.FEEDBACK_FIND_BY_CONDITION,
                match, pageNo, pageSize);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }


    /***
     * @description 更新反馈进度
     */
    @Override
    public JsonObject update(String id, Integer status) {
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        String query = "db.collection(\"feedBack\")"
                .concat(String.format(".doc(\"%s\")" +
                        ".update({data:{status: %d}})", id, status));
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
    public JsonObject delete(String id) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasedelete?access_token=";
        // 数据库查询语句
        String query = "db.collection(\"feedBack\")"
                .concat(String.format(".doc(\"%s\").remove()", id));
        String response = weChatUtil.post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("deleted").getAsInt() == 0) {
            log.warn("未删除任何反馈!");
        }
        return json;
    }

}
