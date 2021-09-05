package com.hitszplaza.background.crawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Slf4j
@Component
public class NewsSchedule {
    @Autowired
    private NewsProcessor newsProcessor;

    @Autowired
    private NewsPipeline newsPipeline;

    @Scheduled(cron = "0 0 1, 13 * * ?")
    public void newsScheduled() {
        log.info("开始新闻抓取任务...");
        Spider.create(newsProcessor)
                .addUrl("https://www.hitsz.edu.cn/article/id-116.html",
                        "https://www.hitsz.edu.cn/article/id-80.html",
                        "https://www.hitsz.edu.cn/article/id-74.html",
                        "https://www.hitsz.edu.cn/article/id-75.html",
                        "https://www.hitsz.edu.cn/article/id-77.html",
                        "https://www.hitsz.edu.cn/article/id-78.html",
                        "https://www.hitsz.edu.cn/article/id-81.html",
                        "https://www.hitsz.edu.cn/article/id-124.html")
                .addPipeline(newsPipeline)
                .setExitWhenComplete(true)
                .thread(8)
                .start();
        log.info("新闻抓取任务完成！");
    }
}
