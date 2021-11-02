package com.hitszplaza.background.controller;

import com.google.gson.JsonObject;
import com.hitszplaza.background.pojo.Group;
import com.hitszplaza.background.service.impl.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupServiceImpl groupService;

    @GetMapping
    public JsonObject find(@RequestParam String id) {
        return groupService.find(id);
    }

    @GetMapping("/all")
    public JsonObject findAllGroup(@RequestParam Integer pageNo,
                                      @RequestParam(defaultValue = "20") Integer pageSize) {
        return groupService.findAll(pageNo - 1, pageSize);
    }

    @PostMapping("/condition")
    public JsonObject findByCondition(@RequestParam Integer pageNo,
                                      @RequestParam(defaultValue = "20") Integer pageSize,
                                      @RequestBody String match) {
        return groupService.findByCondition(pageNo - 1, pageSize, match);
    }

    @PostMapping
    public JsonObject addGroup(@RequestBody Group group) {
        return groupService.add(group);
    }

    @PatchMapping
    public JsonObject updateGroup(@RequestParam String id,
                             @RequestBody String data) {
        return groupService.update(id, data);
    }

    @PatchMapping("status")
    public JsonObject updateGroupStatus(@RequestParam String id,
                                   @RequestParam Boolean valid) {
        return groupService.updateStatus(id, valid);
    }

    @DeleteMapping
    public JsonObject deleteGroup(@RequestParam String id) {
        return groupService.delete(id);
    }
}
