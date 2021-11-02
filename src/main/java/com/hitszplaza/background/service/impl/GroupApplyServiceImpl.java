package com.hitszplaza.background.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.constant.WeChatDataBaseQueryConstant;
import com.hitszplaza.background.pojo.Group;
import com.hitszplaza.background.service.GroupApplyService;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class GroupApplyServiceImpl implements GroupApplyService {

    @Autowired
    private WeChatUtil weChatUtil;

    @Autowired
    private GroupServiceImpl groupService;

    private final int OK = 0;
    private final int WARNING = 1;
    private final int ERROR = 2;
    private final int ACCEPTED = 1;
    private final int DECLINED = 2;

    /***
     * @description 获取特定群聊申请
     */

    @Override
    public JsonObject find(String id) {
        String query = String.format(WeChatDataBaseQueryConstant.GROUPAPPLY_FIND_BY_ID, id);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /***
     * @description 获取所有群聊申请
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     */
    @Override
    public JsonObject findAll(Integer pageNo, Integer pageSize) {
        String query = String.format(WeChatDataBaseQueryConstant.GROUPAPPLY_FIND_ALL, pageNo, pageSize);
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
        String query =  String.format(WeChatDataBaseQueryConstant. GROUPAPPLY_FIND_BY_CONDITION,
                match, pageNo, pageSize);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /**
     * @description 接受申请，提交并发送推送消息
     * @param group 提交的表单信息
     * @param msg 推送消息
     */
    @Override
    public JsonObject accept(String id, String openid, Group group, String msg) {
        int errcode = OK;
        int updateErrcode;
        String errMsg;
//        int submitErrcode;
//        String url = WeChatAPIConstant.WX_API_HOST +
//                "/tcb/databaseadd?access_token=";
//        String query = "db.collection(\"groupApply\")"
//                .concat(String.format(".add({data:[%s]})", data));

        JsonObject res = new JsonObject();

        if (checkStatus(id) != OK) {
            res.addProperty("errcode", ERROR);
            res.addProperty("errmsg", "Already oprated!");
            return res;
        }

        try {
//            String response = weChatUtil.post(url, query);
//            JsonObject json = new Gson().fromJson(response, JsonObject.class);
            JsonObject json = groupService.add(group);
            updateErrcode = updateStatus(id, ACCEPTED);
            //TODO:由公众号向用户推送消息
            if ((json.get("errcode").getAsInt() == OK) && (updateErrcode == OK)) {
                errMsg = "Accept success!";
            } else if ((json.get("errcode").getAsInt() == OK) || (updateErrcode == WARNING)) {
                errcode = WARNING;
                errMsg = "Accept success! But status may be failed to update!";
            } else {
                errcode = ERROR;
                errMsg = "Accept failed!";
            }
            res.addProperty("errcode", errcode);
            res.addProperty("errmsg", errMsg);
        } catch (Exception e) {
            errcode = ERROR;
            errMsg = e.getMessage();
            res.addProperty("errcode", errcode);
            res.addProperty("errmsg", errMsg);
        }
        return res;
    }

    @Override
    public JsonObject decline(String id, String openid, String msg) {
        int errcode;
        int updateErrcode;
        int submitErrcode = OK;
        JsonObject res = new JsonObject();

        if (checkStatus(id) != OK) {
            res.addProperty("errcode", ERROR);
            res.addProperty("errmsg", "Already oprated!");
            return res;
        }

        try {
            updateErrcode = updateStatus(id, DECLINED);
            //TODO:由公众号向用户推送消息
            if (updateErrcode == OK) {
                errcode = OK;
            } else {
                errcode = WARNING;
            }
            res.addProperty("errcode", errcode);
            res.addProperty("errmsg", "Decline success!");
        } catch (Exception e) {
            errcode = ERROR;
            String errmsg = e.getMessage();
            res.addProperty("errcode", errcode);
            res.addProperty("errmsg", errmsg);
        }

        return res;
    }

    /***
     * @description 检测是否重复申请
     */
    private int checkStatus(String id) {
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasequery?access_token=";
        String query = "db.collection(\"groupApply\")"
                .concat(String.format(".doc(\"%s\")" +
                        ".field({status:true}).get()", id));
        String response = weChatUtil.post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        return weChatUtil.toJson(json.get("data").getAsJsonArray().get(0)).get("status").getAsInt();
    }

    /***
     * @description 更新申请状态
     */
    private int updateStatus(String id, Integer status) {
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        String query = "db.collection(\"groupApply\")"
                .concat(String.format(".doc(\"%s\")" +
                        ".update({data:{status: %d}})", id, status));
        String response = weChatUtil.post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("matched").getAsInt() == 0 || json.get("modified").getAsInt() == 0) {
            return WARNING;
        } else {
            return OK;
        }
    }

    /***
     * 推送消息
     */

    private int pushMessage(String id, String openid, String msg) {
        return 0;
    }

    /***
     * @description 删除某个申请
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
        String query = "db.collection(\"groupApply\")"
                .concat(String.format(".doc(\"%s\").remove()", id));
        String response = weChatUtil.post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("deleted").getAsInt() == 0) {
            log.warn("未删除任何申请!");
        }
        return json;
    }
}
