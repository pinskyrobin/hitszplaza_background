package com.hitszplaza.background.controller;

import com.google.gson.JsonObject;
import com.hitszplaza.background.service.impl.UserPosterServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("FieldCanBeLocal")
@RestController
@RequestMapping("/userPoster")
public class UserPosterController {

    @Autowired
    private UserPosterServiceImpl userPosterService;

    private final int PARTIAL_UPDATE = 1;
    private final int ALL_UPDATE = 2;

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
        return userPosterService.updateStatus(PARTIAL_UPDATE, id, valid);
    }

    @PatchMapping("/all")
    public JsonObject updateAllUserPosterStatus(@RequestParam String openid,
                                                @RequestParam Boolean valid) {
        return userPosterService.updateStatus(ALL_UPDATE, openid, valid);
    }

    @DeleteMapping
    public JsonObject deleteUserPoster(@RequestParam String id) {
        return userPosterService.deleteOne(id);
    }
}
