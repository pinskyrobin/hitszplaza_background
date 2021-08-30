package com.hitszplaza.background.service;

import com.google.gson.JsonObject;

public interface UserInfoService {
    JsonObject find(String openId);

    JsonObject findAll(Integer pageNo, Integer pageSize);

    JsonObject findByCondition(Integer pageNo, Integer pageSize, String match);

    JsonObject updateStatus(String openId, String property, Boolean valid);
}
