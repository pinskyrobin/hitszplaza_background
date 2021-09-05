package com.hitszplaza.background.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.hitszplaza.background.crawler.NewsPipeline;
import com.hitszplaza.background.crawler.NewsProcessor;
import com.hitszplaza.background.mapper.NewsMapper;
import com.hitszplaza.background.pojo.News;
import com.hitszplaza.background.service.NewsService;
import com.hitszplaza.background.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.util.Objects;

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    private final Integer SELECT_ALL = -1;

    @Autowired
    private NewsProcessor newsProcessor;

    @Autowired
    private NewsPipeline newsPipeline;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private RedisUtil redisUtil;

    /***
     * @description 计数
     */
    @Override
    public JsonObject countNews(Integer category) {
        JsonObject response = new JsonObject();
        int count = (Objects.equals(category, SELECT_ALL)) ? newsMapper.countAll() : newsMapper.count(category);
        response.addProperty("errcode", 0);
        response.addProperty("errmsg", count);
        return response;
    }

    /***
     * @description 获取数据库里的新闻
     * @param category 分类代码，若为 -1 则表示查找全部
     */
    @Override
    public PageInfo<News> findNews(int pageNo, int pageSize, Integer category) {
        PageHelper.startPage(pageNo, pageSize);
        if (Objects.equals(category, SELECT_ALL)) {
            return new PageInfo<>(newsMapper.findAll());
        } else {
            return new PageInfo<>(newsMapper.find(category));
        }
    }

    /***
     * @description 获取特定状态的资讯
     */
    @Override
    public PageInfo<News> findNewsByStatus(int pageNo, int pageSize, Integer category, Boolean valid) {
        PageHelper.startPage(pageNo, pageSize);
        if (Objects.equals(category, SELECT_ALL)) {
            return new PageInfo<>(newsMapper.findAllByStatus(valid));
        } else {
            return new PageInfo<>(newsMapper.findByStatus(category, valid));
        }
    }

    /***
     * @description 更新新闻
     * @param category 分类代码，具体映射关系见 NewsConstant 类，如该值等于-1，则表示获取全部分类的更新
     */
    @Override
    public JsonObject updateNews(Integer category) {
        JsonObject response = new JsonObject();
        int beforeUpdate = countNews(category).get("errmsg").getAsInt();
        try {
            Spider spider = Spider.create(newsProcessor);
            if (Objects.equals(category, SELECT_ALL)) {
                spider.addUrl("https://www.hitsz.edu.cn/article/id-116.html",
                        "https://www.hitsz.edu.cn/article/id-80.html",
                        "https://www.hitsz.edu.cn/article/id-74.html",
                        "https://www.hitsz.edu.cn/article/id-75.html",
                        "https://www.hitsz.edu.cn/article/id-77.html",
                        "https://www.hitsz.edu.cn/article/id-78.html",
                        "https://www.hitsz.edu.cn/article/id-81.html",
                        "https://www.hitsz.edu.cn/article/id-124.html");
            } else {
                spider.addUrl(String.format("https://www.hitsz.edu.cn/article/id-%d.html", category));
            }
            spider.addPipeline(newsPipeline)
                    .setExitWhenComplete(true)
                    .thread(8)
                    .run();
            int afterUpdate = countNews(category).get("errmsg").getAsInt();
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
     * @description 更新新闻的状态
     */
    @Override
    public JsonObject updateNewsStatus(Integer id, Boolean valid) {
        JsonObject response = new JsonObject();
        try {
            int num = newsMapper.updateStatus(id, valid);
            response.addProperty("errcode", 0);
            if (num == 0) {
                response.addProperty("errmsg", "未更新任何资讯！");
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
     * @description 删除新闻
     */
    @Override
    public JsonObject deleteNews(Integer id) {
        JsonObject response = new JsonObject();
        try {
            int category = newsMapper.getCategory(id);
            String key = "fingerprint_news_" + category;
            redisUtil.removeSet(key, String.valueOf(id));
            int num = newsMapper.delete(id);
            if (num == 0) {
                response.addProperty("errmsg", "未删除任何资讯！");
            } else {
                response.addProperty("errmsg", "删除成功！");
            }
            response.addProperty("errcode", 0);
            response.addProperty("errmsg", "删除成功！");
        } catch (Exception e) {
            response.addProperty("errcode", 1);
            response.addProperty("errmsg", "删除失败！原因：" + e.getMessage());
        }
        return response;
    }
}
