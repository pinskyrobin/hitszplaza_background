package com.hitszplaza.background.controller;

import com.github.pagehelper.PageInfo;
import com.hitszplaza.background.pojo.Swiper;
import com.hitszplaza.background.service.impl.SwiperServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/swiper")
public class SwiperController {
    @Autowired
    private SwiperServiceImpl swiperService;

    @GetMapping
    public PageInfo<Swiper> findAllSwiper(@RequestParam Integer pageNo,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return swiperService.findAll(pageNo, pageSize);
    }

    @GetMapping("/status")
    public PageInfo<Swiper> findSwiperByStatus(@RequestParam Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam Integer status) {
        return swiperService.find(pageNo, pageSize, status);
    }

    @PostMapping("/uploadFile")
    public JSONObject uploadSwiper(@RequestParam MultipartFile file) {
        return swiperService.uploadSwiper(file);
    }

    @PostMapping
    public void addSwiper(@RequestBody Swiper swiper) {
        swiperService.addSwiper(swiper);
    }

    @DeleteMapping
    public void deleteSwiper(@RequestParam Integer swiperId) {
        swiperService.delete(swiperId);
    }

}