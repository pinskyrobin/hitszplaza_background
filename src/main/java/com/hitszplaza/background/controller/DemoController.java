package com.hitszplaza.background.controller;

import com.hitszplaza.background.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/accessToken")
    public String demo() {
        return redisUtil.get("accessToken");
    }

}
