package com.hitszplaza.background.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.constant.WeChatDataBaseQueryConstant;
import com.hitszplaza.background.utils.WeChatUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * 通用控制接口,用于处理一些通用的请求
 */

@RestController
@RequestMapping("/")
public class UniversalController {

    @Autowired
    private WeChatUtil weChatUtil;

    @GetMapping("/count")
    public JsonObject counter(@RequestParam String database,
                              @RequestBody String match) {
        String query = String.format(WeChatDataBaseQueryConstant.COUNTER, database, match);
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databaseaggregate?access_token=";
        System.out.println(query);
        String response = weChatUtil.post(url, query);
        return new Gson().fromJson(response, JsonObject.class);
    }

    @PatchMapping("/status")
    public JsonObject updateStatus(@RequestParam String id,
                                   @RequestParam Integer status,
                                   @RequestParam String database) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databaseupdate?access_token=";
        // 数据库查询语句
        String query = String.format("db.collection(\"%s\").doc(\"%s\")" +
                ".update({data:{status: %d}})", database, id, status);
        return weChatUtil.update(url, query);
    }

    @DeleteMapping
    public JsonObject delete(@RequestParam String id,
                             @RequestParam String database) {
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasedelete?access_token=";
        // 数据库查询语句
        String query = String.format("db.collection(\"%s\").doc(\"%s\").remove()", database, id);
        return weChatUtil.delete(url, query);
    }


}
