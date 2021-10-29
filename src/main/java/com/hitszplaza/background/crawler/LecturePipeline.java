package com.hitszplaza.background.crawler;

import com.hitszplaza.background.mapper.LectureMapper;
import com.hitszplaza.background.mapper.NewsMapper;
import com.hitszplaza.background.pojo.Lecture;
import com.hitszplaza.background.pojo.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
public class LecturePipeline implements Pipeline {
    @Resource
    LectureMapper lectureMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().matches("lecture(.*)")) {
                Lecture lecture = (Lecture) entry.getValue();
                lectureMapper.insert(lecture);
            }
        }
    }
}
