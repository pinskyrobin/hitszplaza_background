package com.hitszplaza.background.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.constant.WeChatDataBaseQueryConstant;
import com.hitszplaza.background.pojo.Group;
import com.hitszplaza.background.service.GroupService;
import com.hitszplaza.background.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private WeChatUtil weChatUtil;

    /***
     * @description 获取特定群聊
     */

    @Override
    public JsonObject find(String id) {
        String query = String.format(WeChatDataBaseQueryConstant.GROUP_FIND_BY_ID, id);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /***
     * @description 获取所有群聊
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     */
    @Override
    public JsonObject findAll(Integer pageNo, Integer pageSize) {
        String query = String.format(WeChatDataBaseQueryConstant.GROUP_FIND_ALL, pageNo, pageSize);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /**
     *  添加群组信息
     */
    @Override
    public JsonObject add(Group group) {
        if (group.getValid() == null) {
            group.setValid(true);
        }
        if (group.getCreateTime() == null) {
            long curr = new Date().getTime();
            group.setCreateTime(curr);
        }

        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseadd?access_token=";
        String grp = new Gson().toJson(group);
        String query = "db.collection(\"group\")"
                .concat(String.format(".add({data:[%s]})", grp));
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    /***
     * @description 条件查找
     * @param match 筛选语句
     */
    @Override
    public JsonObject findByCondition(Integer pageNo, Integer pageSize, String match) {
        String query =  String.format(WeChatDataBaseQueryConstant. GROUP_FIND_BY_CONDITION,
                match, pageNo, pageSize);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }


    /***
     * @description 更新群组信息
     */
    @Override
    public JsonObject update(String id, String data) {
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        String query = "db.collection(\"group\")"
                .concat(String.format(".doc(\"%s\")" +
                        ".update({data:%s})", id, data));
        String response = weChatUtil.post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("matched").getAsInt() == 0) {
            log.warn("未匹配到任何记录");
        } else if (json.get("modified").getAsInt() == 0) {
            log.warn("匹配到记录，但未修改任何记录");
        }
        return json;
    }

    @Override
    public JsonObject updateStatus(String id, Boolean valid) {
        String tmpValid = valid ? "true" : "false";
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        String query = "db.collection(\"group\")"
                .concat(String.format(".doc(\"%s\")" +
                        ".update({data:{valid: %s}})", id, tmpValid));
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
     * @description 删除某个群组
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
        String query = "db.collection(\"group\")"
                .concat(String.format(".doc(\"%s\").remove()", id));
        String response = weChatUtil.post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("deleted").getAsInt() == 0) {
            log.warn("未删除任何群组!");
        }
        return json;
    }
}
