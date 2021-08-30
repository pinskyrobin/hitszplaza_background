package com.hitszplaza.background.controller;

import com.hitszplaza.background.pojo.Swiper;
import com.hitszplaza.background.service.impl.SwiperServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/swiper", produces = "application/json;charset=utf-8")
public class SwiperController {
    @Autowired
    private SwiperServiceImpl swiperService;

    @GetMapping
    public List<Swiper> findAllSwiper() {
        return swiperService.find(null);
    }

    @GetMapping("/id")
    public String findSwiperBySwiperId(@RequestParam Integer swiperId) {
        return swiperService.findBySwiperId(swiperId).toString();
    }

    @GetMapping("/status")
    public List<Swiper> findSwiperByStatus(@RequestParam Integer status) {
        return swiperService.find(status);
    }

    @PostMapping("/uploadFile")
    public String uploadSwiper(@RequestParam MultipartFile file) {
        return swiperService.uploadSwiper(file).toString();
    }

    @PostMapping
    public String addSwiper(@RequestBody Swiper swiper) {
        JSONObject response = new JSONObject();
        Integer status = swiperService.add(swiper);
        response.put("errcode", status);
        if (status == 0) {
            response.put("errmsg", "添加成功！");
        } else if (status == -1) {
            response.put("errmsg", "添加失败！swiper 数据上传至数据库时出错！");
        } else if (status == -2) {
            response.put("errmsg", "添加失败！倒计时设置出错！可能是设定时间早于当前时间！");
        } else {
            response.put("errmsg", "添加失败！");
        }
        return response.toString();
    }

    @PatchMapping
    public String updateSwiper(@RequestBody Swiper swiper) {
        JSONObject response = new JSONObject();
        Integer status = swiperService.update(swiper);
        response.put("errcode", status);
        if (status == 0) {
            response.put("errmsg", "更新成功！");
        } else {
            response.put("errmsg", "更新失败，原因见日志文件！");
        }
        return response.toString();
    }

    @PatchMapping("status")
    public String updateSwiperStatus(@RequestParam Integer swiperId,
                                     @RequestParam Integer nextStatus) {
        JSONObject response = new JSONObject();
        Boolean status = swiperService.updateStatus(swiperId, nextStatus);
        Integer errcode = (status) ? 0 : -1;
        response.put("errcode", errcode);
        if (status) {
            response.put("errmsg", "更新状态成功！");
        } else {
            response.put("errmsg", "更新状态失败，原因见日志文件！");
        }
        return response.toString();
    }

    @DeleteMapping
    public String deleteSwiper(@RequestParam Integer swiperId) {
        JSONObject response = new JSONObject();
        Boolean status = swiperService.delete(swiperId);
        Integer errcode = (status) ? 0 : -1;
        response.put("errcode", errcode);
        if (status) {
            response.put("errmsg", "删除成功！");
        } else {
            response.put("errmsg", "删除失败，原因见日志文件！");
        }
        return response.toString();
    }

}