package com.hitszplaza.background.constant;

import java.util.HashMap;
import java.util.Map;

public interface CrawlConstant {
    String BASE_URL = "https://www.hitsz.edu.cn";
    String NEWS_URL = "https://www.hitsz.edu.cn/article/id-.*";
    String LECTURE_URL = "https://www.hitsz.edu.cn/article/id-78.html";
    String NEWS_CONTENT_URL = "https://www.hitsz.edu.cn/article/view/id-.*";

    Map<Integer, String> categoryMap = new HashMap<Integer, String>(){{
        categoryMap.put(116, "校区要闻");
        categoryMap.put(80, "媒体报道");
        categoryMap.put(74, "通知公告");
        categoryMap.put(75, "综合新闻");
        categoryMap.put(77, "校园动态");
        categoryMap.put(78, "讲座论坛");
        categoryMap.put(79, "热点专题");
        categoryMap.put(81, "招标信息");
        categoryMap.put(124, "重要关注");
    }};
}
