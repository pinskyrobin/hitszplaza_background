package com.hitszplaza.background.crawler;

import com.hitszplaza.background.mapper.NewsMapper;
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
public class NewsPipeline implements Pipeline {

    @Resource
    NewsMapper newsMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().matches("news(.*)")) {
                News news=(News) entry.getValue();
                newsMapper.insert(news);
            }
        }
    }
}
