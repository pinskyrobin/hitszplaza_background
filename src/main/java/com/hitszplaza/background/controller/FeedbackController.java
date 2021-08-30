package com.hitszplaza.background.controller;

import com.google.gson.JsonObject;
import com.hitszplaza.background.service.impl.FeedbackServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedback")
public class FeedbackController {

    @Autowired
    private FeedbackServiceImpl feedbackService;

    @GetMapping
    public JsonObject find(@RequestParam String id) {
        return feedbackService.find(id);
    }

    @GetMapping("/all")
    public JsonObject findAllFeedback(@RequestParam Integer pageNo,
                                      @RequestParam(defaultValue = "20") Integer pageSize) {
        return feedbackService.findAll(pageNo - 1, pageSize);
    }

    @GetMapping("/condition")
    public JsonObject findByCondition(@RequestParam Integer pageNo,
                                      @RequestParam(defaultValue = "20") Integer pageSize,
                                      @RequestBody String match) {
        return feedbackService.findByCondition(pageNo - 1, pageSize, match);
    }

    @DeleteMapping
    public JsonObject deleteUserPoster(@RequestParam String id) {
        return feedbackService.delete(id);
    }
}