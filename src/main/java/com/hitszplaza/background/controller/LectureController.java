package com.hitszplaza.background.controller;

import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.hitszplaza.background.pojo.Lecture;
import com.hitszplaza.background.service.impl.LectureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lecture")
public class LectureController {

    @Autowired
    private LectureServiceImpl lectureService;

    @GetMapping("/count")
    public JsonObject count() {
        return lectureService.countLecture();
    }

    @GetMapping
    public JsonObject findLecture(@RequestParam Integer id) {
        return lectureService.findLecture(id);
    }

    @GetMapping("/all")
    public PageInfo<Lecture> findAllLecture(@RequestParam Integer pageNo,
                                            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return lectureService.findAllLecture(pageNo, pageSize);
    }

    @GetMapping("/status")
    public PageInfo<Lecture> findLectureByStatus(@RequestParam Integer pageNo,
                                                @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                                @RequestParam Boolean valid) {
        return lectureService.findLectureByStatus(pageNo, pageSize, valid);
    }

    @PatchMapping
    public JsonObject updateLecture() {
        return lectureService.updateLecture();
    }

    @PatchMapping("/status")
    public JsonObject updateLectureStatus(@RequestParam Integer id,
                                          @RequestParam Boolean valid) {
        return lectureService.updateLectureStatus(id, valid);
    }

    @DeleteMapping
    public JsonObject deleteLecture(@RequestParam Integer id) {
        return lectureService.deleteLecture(id);
    }

}
