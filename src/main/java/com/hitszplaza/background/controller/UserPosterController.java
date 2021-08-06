package com.hitszplaza.background.controller;

import com.hitszplaza.background.service.impl.UserPosterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userPoster")
public class UserPosterController {

    @Autowired
    private UserPosterServiceImpl userPosterService;

    @GetMapping("/all")
    public String findAllUserPoster(@RequestParam Integer pageNo,
                                    @RequestParam(defaultValue = "20") Integer pageSize) {
        return userPosterService.findAll(pageNo, pageSize);
    }

    @GetMapping("/openId")
    public String findUserPosterByOpenId(@RequestParam Integer pageNo,
                                         @RequestParam(defaultValue = "20") Integer pageSize,
                                         @RequestParam String openid) {
        return userPosterService.findAllWithOpenId(pageNo, pageSize, openid);
    }

    //TODO:关于日期格式的问题,待验证
    @GetMapping("/time")
    public String findUserPosterByDate(@RequestParam Integer pageNo,
                                       @RequestParam(defaultValue = "20") Integer pageSize,
                                       @RequestParam Long startTime,
                                       @RequestParam Long endTime) {
        return userPosterService.findAllWithTime(pageNo, pageSize, startTime, endTime);
    }

    @GetMapping("/type")
    public String findUserPosterByType(@RequestParam Integer pageNo,
                                       @RequestParam(defaultValue = "20") Integer pageSize,
                                       @RequestParam String type) {
        return userPosterService.findAllWithType(pageNo, pageSize, type);
    }

    @GetMapping("/anonymous")
    public String findUserPosterByAnonymous(@RequestParam Integer pageNo,
                                            @RequestParam(defaultValue = "20") Integer pageSize) {
        return userPosterService.findAllWithAnoymous(pageNo, pageSize);
    }

    @PatchMapping("/oneStatus")
    public String updateUserPosterStatus(@RequestParam String id,
                                         @RequestParam Boolean valid) {
        return userPosterService.updateOneStatus(id, valid);
    }

    @PatchMapping("/allStatus")
    public String updateAllUserPosterStatus(@RequestParam String openid,
                                            @RequestParam Boolean valid) {
        return userPosterService.updateAllStatusWithOpenId(openid, valid);
    }

    @DeleteMapping("/")
    public String deleteUserPoster(@RequestParam String id) {
        return userPosterService.deleteOne(id);
    }
}
