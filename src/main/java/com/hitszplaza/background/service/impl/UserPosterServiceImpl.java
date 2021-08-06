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

    /***
     * @description 获取所有userPoster
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     * @return List/<Object/>形式的jsonArray数组,每一个元素为查询所得记录
     */
    @Override
    public String findAll(Integer pageNo, Integer pageSize) {
        // 数据库查询语句
        String preQuery = "db.collection(\"userPoster\")";
        return weChatUtil.pageQuery(pageNo, pageSize, preQuery);
    }

    /***
     * @description 获取某用户所有userPoster
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     * @param openId 用户标识_openid
     * @return 同findAll方法
     */
    @Override
    public String findAllWithOpenId(Integer pageNo, Integer pageSize, String openId) {
        // 数据库查询语句
        String preQuery = String.format("db.collection(\"userPoster\").where({openid:\"%s\"})", openId);
        return weChatUtil.pageQuery(pageNo, pageSize, preQuery);
    }

    /***
     * @description 根据类型获取userPoster
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     * @param type userPoster的类型
     * @return 同findAll方法
     */
    @Override
    public String findAllWithType(Integer pageNo, Integer pageSize, String type) {
        // 数据库查询语句
        String preQuery = String.format("db.collection(\"userPoster\").where({type:\"%s\"})", type);
        return weChatUtil.pageQuery(pageNo, pageSize, preQuery);
    }

    /***
     * @description 根据时间获取userPoster
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     * @param startTime 起始时间(long int类型)
     * @param endTime 终止时间(long int类型)
     * @return 同findAll方法
     */
    @Override
    public String findAllWithTime(Integer pageNo, Integer pageSize, Long startTime, Long endTime) {
        // 数据库查询语句
        String preQuery = String.format("db.collection(\"userPoster\")" +
                ".where({time:_.gt(%d).and(_.lt(%d)})", startTime, endTime);
        return weChatUtil.pageQuery(pageNo, pageSize, preQuery);
    }

    /***
     * @description 根据类型获取userPoster
     * @param pageNo 分页的页数
     * @param pageSize 分页大小
     * @return 同findAll方法
     */
    @Override
    public String findAllWithAnoymous(Integer pageNo, Integer pageSize) {
        // 数据库查询语句
        String preQuery = "db.collection(\"userPoster\").where({isAnoymous: true})";
        return weChatUtil.pageQuery(pageNo, pageSize, preQuery);
    }

    /***
     * @description 更新某个userPoster状态
     * @param id 主键_id
     * @param valid 待更新的valid字段,true = 有效,false = 无效
     * @return 包含errcode,errmsg,matched,modified和id的response返回体,
     *          matched为更新条件匹配到的结果数.
     *          modified为修改的记录数，注意：使用set操作新插入的数据不计入修改数目.
     *          id为新插入记录的id，注意：只有使用set操作新插入数据时这个字段会有值.
     */
    @Override
    public String updateOneStatus(String id, Boolean valid) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        String tmpValid = valid ? "true" : "false";
        // 数据库查询语句,
        String query = "db.collection(\"userPoster\")"
                .concat(String.format(".doc(\"%s\")" +
                        ".update({data:{valid: %s}})", id, tmpValid));
        String response = weChatUtil.post(url, query);
        JSONObject json = new JSONObject(response);
        if ((Integer) json.get("matched") == 0) {
            log.warn("未匹配到任何记录");
        } else if ((Integer) json.get("modified") == 0) {
            log.warn("匹配到记录，但未修改任何记录");
        }
        return response;
    }

    /***
     * @description 更新某用户所有userPoster的状态
     * @param openId 用户标识_openid
     * @param valid 待更新的valid字段,true = 有效,false = 无效
     * @return 同updateOneStatus方法
     */
    //TODO: 在对该用户所发帖子进行屏蔽时,是否同时对其评论进行屏蔽?
    @Override
    public String updateAllStatusWithOpenId(String openId, Boolean valid) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        String tmpValid = valid ? "true" : "false";
        // 数据库查询语句
        String query = "db.collection(\"userPoster\")"
                .concat(String.format(".where({openid: %s})" +
                        ".update({data:{valid: %s}})", openId, tmpValid));
        String response = weChatUtil.post(url, query);
        JSONObject json = new JSONObject(response);
        if ((Integer) json.get("matched") == 0) {
            log.warn("未匹配到任何记录");
        } else if ((Integer) json.get("modified") == 0) {
            log.warn("未修改任何记录");
        }
        return response;
    }

    /***
     * @description 删除某个userPoster
     * @param id 主键_id
     * @return 包含errcode,errmsg和deleted的response返回体.
     *          deleted为删除记录数量.
     */
    @Override
    public String deleteOne(String id) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasedelete?access_token=";
        // 数据库查询语句
        String query = "db.collection(\"userPoster\")"
                .concat(String.format(".doc(\"%s\").remove()", id));
        String response = weChatUtil.post(url, query);
        JSONObject json = new JSONObject(response);
        if ((Integer) json.get("deleted") == 0) {
            log.warn("未删除任何记录!");
        }
        return response;
    }
}
