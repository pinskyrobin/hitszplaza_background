package com.hitszplaza.background.crawler;

import com.hitszplaza.background.constant.CrawlConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Slf4j
@Component
public class LectureSchedule {
    @Autowired
    private LectureProcessor lectureProcessor;

    @Autowired
    private LecturePipeline lecturePipeline;

    @Scheduled(cron = "0 0 2,14 * * ?")
    public void lectureScheduled() {
        log.info("开始讲座抓取任务...");
        Spider.create(lectureProcessor)
                .addUrl(CrawlConstant.LECTURE_URL)
                .addPipeline(lecturePipeline)
                .setExitWhenComplete(true)
                .start();
        log.info("讲座抓取任务完成！");
    }
}
