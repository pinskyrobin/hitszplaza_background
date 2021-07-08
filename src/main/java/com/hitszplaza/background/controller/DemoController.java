package com.hitszplaza.background.controller;

import com.hitszplaza.background.service.impl.UserInfoServiceImpl;
import com.hitszplaza.background.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @GetMapping("/accessToken")
    public String demo() {
        return redisUtil.get("accessToken");
    }

    @PostMapping("/userInfo")
    public String getUserInfo(@RequestParam(required = false) Integer number) {
        return userInfoService.findAll(number);
    }
}
