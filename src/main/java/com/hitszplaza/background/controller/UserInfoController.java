package com.hitszplaza.background.controller;

import com.google.gson.JsonObject;
import com.hitszplaza.background.service.impl.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoServiceImpl userInfoService;

    @GetMapping("/all")
    public JsonObject findAll(@RequestParam Integer pageNo,
                              @RequestParam(defaultValue = "20") Integer pageSize) {
        return userInfoService.findAll(pageNo - 1, pageSize);
    }

    @GetMapping("/")
    public JsonObject find(@RequestParam String openId) {
        return userInfoService.find(openId);
    }

    @PostMapping("/condition")
    public JsonObject findByCondition(@RequestParam Integer pageNo,
                                      @RequestParam(defaultValue = "20") Integer pageSize,
                                      @RequestBody String match) {
        return userInfoService.findByCondition(pageNo - 1, pageSize, match);
    }

    @PatchMapping("/status")
    public JsonObject updateStatus(@RequestParam String openId,
                                   @RequestParam String property,
                                   @RequestParam Boolean valid) {
        return userInfoService.updateStatus(openId, property, valid);
    }
}
