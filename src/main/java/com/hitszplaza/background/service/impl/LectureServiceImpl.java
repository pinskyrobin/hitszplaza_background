package com.hitszplaza.background.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.CrawlConstant;
import com.hitszplaza.background.crawler.LecturePipeline;
import com.hitszplaza.background.crawler.LectureProcessor;
import com.hitszplaza.background.mapper.LectureMapper;
import com.hitszplaza.background.pojo.Lecture;
import com.hitszplaza.background.service.LectureService;
import com.hitszplaza.background.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

@Service
@Slf4j
public class LectureServiceImpl implements LectureService {

    @Autowired
    private LectureProcessor lectureProcessor;

    @Autowired
    private LecturePipeline lecturePipeline;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private RedisUtil redisUtil;

    /***
     * @description 计数
     */
    @Override
    public JsonObject countLecture() {
        JsonObject response = new JsonObject();
        int count = lectureMapper.count();
        response.addProperty("errcode", 0);
        response.addProperty("errmsg", count);
        return response;
    }

    /***
     * @description 获取特定的讲座
     */
    public JsonObject findLecture(Integer id) {
        JsonObject response = new JsonObject();
        Lecture lecture = lectureMapper.find(id);
        response.addProperty("errcode", 0);
        JsonElement jsonElement = new Gson().toJsonTree(lecture, lecture.getClass());
        response.add("errmsg", jsonElement);
        return response;
    }

    /***
     * @description 获取数据库里的讲座
     */
    @Override
    public PageInfo<Lecture> findAllLecture(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return new PageInfo<>(lectureMapper.findAll());
    }

    /***
     * @description 获取特定状态的讲座
     */
    @Override
    public PageInfo<Lecture> findLectureByStatus(int pageNo, int pageSize, Boolean valid) {
        PageHelper.startPage(pageNo, pageSize);
        return new PageInfo<>(lectureMapper.findByStatus(valid));
    }

    /***
     * @description 更新讲座
     */
    @Override
    public JsonObject updateLecture() {
        JsonObject response = new JsonObject();
        int beforeUpdate = countLecture().get("errmsg").getAsInt();
        try {
            Spider.create(lectureProcessor)
                    .addUrl(CrawlConstant.LECTURE_URL)
                    .addPipeline(lecturePipeline)
                    .setExitWhenComplete(true)
                    .run();
            int afterUpdate = countLecture().get("errmsg").getAsInt();
            response.addProperty("errcode", 0);
            response.addProperty("errmsg", afterUpdate - beforeUpdate);
        } catch (Exception e) {
            response.addProperty("errcode", 1);
            response.addProperty("errmsg", "更新失败！原因：" + e.getMessage());
            log.error(e.getMessage());
        }
        return response;
    }

    /***
     * @description 更新讲座的状态
     */
    @Override
    public JsonObject updateLectureStatus(Integer id, Boolean valid) {
        JsonObject response = new JsonObject();
        try {
            int num = lectureMapper.updateStatus(id, valid);
            response.addProperty("errcode", 0);
            if (num == 0) {
                response.addProperty("errmsg", "未更新任何讲座！");
            } else {
                response.addProperty("errmsg", "更新成功！");
            }
        } catch (Exception e) {
            response.addProperty("errcode", 1);
            response.addProperty("errmsg", "更新失败！原因：" + e.getMessage());
        }
        return response;
    }

    /***
     * @description 删除讲座
     */
    @Override
    public JsonObject deleteLecture(Integer id) {
        JsonObject response = new JsonObject();
        try {
            String key = "fingerprint_lecture";
            redisUtil.removeSet(key, String.valueOf(id));
            int num = lectureMapper.delete(id);
            if (num == 0) {
                response.addProperty("errmsg", "未删除任何讲座！");
            } else {
                response.addProperty("errmsg", "删除成功！");
            }
            response.addProperty("errcode", 0);
        } catch (Exception e) {
            response.addProperty("errcode", 1);
            response.addProperty("errmsg", "删除失败！原因：" + e.getMessage());
        }
        return response;
    }
}
