package com.hitszplaza.background.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.hitszplaza.background.pojo.Lecture;

public interface LectureService {
    JsonObject countLecture();
    JsonObject findLecture(Integer id);
    PageInfo<Lecture> findAllLecture(int pageNo, int pageSize);
    PageInfo<Lecture> findLectureByStatus(int pageNo, int pageSize, Boolean valid);
    JsonObject updateLecture();
    JsonObject updateLectureStatus(Integer id, Boolean valid);
    JsonObject deleteLecture(Integer id);
}
