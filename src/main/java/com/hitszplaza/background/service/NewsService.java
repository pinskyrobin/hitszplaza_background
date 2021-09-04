package com.hitszplaza.background.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.hitszplaza.background.pojo.News;

public interface NewsService {
    JsonObject countNews(Integer category);
    PageInfo<News> findNews(int pageNo, int pageSize, Integer category);
    PageInfo<News> findNewsByStatus(int pageNo, int pageSize, Integer category, Boolean valid);
    JsonObject updateNews(Integer category);
    JsonObject updateNewsStatus(Integer id, Boolean valid);
    JsonObject deleteNews(Integer id);
}
