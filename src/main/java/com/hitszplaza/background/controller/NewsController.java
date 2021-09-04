package com.hitszplaza.background.controller;

import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.hitszplaza.background.pojo.News;
import com.hitszplaza.background.service.impl.NewsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsServiceImpl newsService;

    @GetMapping("/count")
    public JsonObject count(@RequestParam(required = false, defaultValue = "-1") Integer category) {
        return newsService.countNews(category);
    }

    @GetMapping
    public PageInfo<News> findNews(@RequestParam Integer pageNo,
                                   @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "-1") Integer category) {
        return newsService.findNews(pageNo, pageSize, category);
    }

    @GetMapping("/status")
    public PageInfo<News> findNewsByStatus(@RequestParam Integer pageNo,
                                           @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                           @RequestParam(required = false, defaultValue = "-1") Integer category,
                                           @RequestParam Boolean valid) {
        return newsService.findNewsByStatus(pageNo, pageSize, category, valid);
    }

    @PatchMapping
    public JsonObject updateNews(@RequestParam(required = false, defaultValue = "-1") Integer category) {
        return newsService.updateNews(category);
    }


    @PatchMapping("/status")
    public JsonObject updateNewsStatus(@RequestParam Integer id,
                                       @RequestParam Boolean valid) {
        return newsService.updateNewsStatus(id, valid);
    }

    @DeleteMapping
    public JsonObject deleteNews(@RequestParam Integer id) {
        return newsService.deleteNews(id);
    }
}
