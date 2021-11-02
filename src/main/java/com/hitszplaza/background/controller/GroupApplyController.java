package com.hitszplaza.background.controller;

import com.google.gson.JsonObject;
import com.hitszplaza.background.pojo.Group;
import com.hitszplaza.background.service.impl.GroupApplyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groupApply")
public class GroupApplyController {

    @Autowired
    private GroupApplyServiceImpl groupApplyService;

    @GetMapping
    public JsonObject find(@RequestParam String id) {
        return groupApplyService.find(id);
    }

    @GetMapping("/all")
    public JsonObject findAllGroupApply(@RequestParam Integer pageNo,
                                      @RequestParam(defaultValue = "20") Integer pageSize) {
        return groupApplyService.findAll(pageNo - 1, pageSize);
    }

    @PostMapping("/condition")
    public JsonObject findByCondition(@RequestParam Integer pageNo,
                                      @RequestParam(defaultValue = "20") Integer pageSize,
                                      @RequestBody String match) {
        return groupApplyService.findByCondition(pageNo - 1, pageSize, match);
    }

    @PostMapping("accept")
    public JsonObject acceptApply(@RequestParam String id,
                                   @RequestParam(required = false) String openid,
                                   @RequestBody Group group,
                                   @RequestParam(required = false) String msg) {
        return groupApplyService.accept(id, openid, group, msg);
    }

    @PostMapping("decline")
    public JsonObject declineApply(@RequestParam String id,
                                  @RequestParam(required = false) String openid,
                                  @RequestParam(required = false) String msg) {
        return groupApplyService.decline(id, openid, msg);
    }

    @DeleteMapping
    public JsonObject deleteApply(@RequestParam String id) {
        return groupApplyService.delete(id);
    }
}
