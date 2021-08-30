package com.hitszplaza.background.service;

import com.google.gson.JsonObject;

public interface FeedbackService {
    JsonObject find(String id);

    JsonObject findAll(Integer pageNo, Integer pageSize);

    JsonObject findByCondition(Integer pageNo, Integer pageSize, String match);

    JsonObject update(String id, Integer status);

    JsonObject delete(String id);
}
