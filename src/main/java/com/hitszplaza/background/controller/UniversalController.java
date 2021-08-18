package com.hitszplaza.background.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.constant.WeChatDataBaseQueryConstant;
import com.hitszplaza.background.utils.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return new Gson().fromJson(response, JsonObject .class);
    }

}
