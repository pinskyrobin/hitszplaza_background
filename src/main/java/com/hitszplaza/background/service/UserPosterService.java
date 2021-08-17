package com.hitszplaza.background.service;

import com.google.gson.JsonObject;

public interface UserPosterService {
    JsonObject count(String match);

    JsonObject find(String id);

    JsonObject findAll(Integer pageNo, Integer pageSize);

    JsonObject findByCondition(Integer pageNo, Integer pageSize, String match);

    JsonObject updateStatus(Integer mode, String id, Boolean valid);

    JsonObject deleteOne(String id);
}
