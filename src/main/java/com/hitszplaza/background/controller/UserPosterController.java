package com.hitszplaza.background.controller;

import com.google.gson.JsonObject;
import com.hitszplaza.background.service.impl.UserPosterServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userPoster")
public class UserPosterController {

    @Autowired
    private UserPosterServiceImpl userPosterService;

    @GetMapping
    public JsonObject find(@RequestParam String id) {
        return userPosterService.find(id);
    }

    @GetMapping("/all")
    public JsonObject findAllUserPoster(@RequestParam Integer pageNo,
                                        @RequestParam(defaultValue = "20") Integer pageSize) {
        return userPosterService.findAll(pageNo - 1, pageSize);
    }

    @GetMapping("/condition")
    public JsonObject findByCondition(@RequestParam Integer pageNo,
                                      @RequestParam(defaultValue = "20") Integer pageSize,
                                      @RequestBody String match) {
        return userPosterService.findByCondition(pageNo - 1, pageSize, match);
    }

    @PatchMapping
    public JsonObject updateUserPosterStatus(@RequestParam String id,
                                             @RequestParam Boolean valid) {
        return userPosterService.updateStatus(1, id, valid);
    }

    @PatchMapping("/all")
    public JsonObject updateAllUserPosterStatus(@RequestParam String openid,
                                                @RequestParam Boolean valid) {
        return userPosterService.updateStatus(2, openid, valid);
    }

    @DeleteMapping
    public JsonObject deleteUserPoster(@RequestParam String id) {
        return userPosterService.deleteOne(id);
    }
}
