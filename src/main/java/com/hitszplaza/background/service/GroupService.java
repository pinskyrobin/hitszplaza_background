package com.hitszplaza.background.service;

import com.google.gson.JsonObject;
import com.hitszplaza.background.pojo.Group;

public interface GroupService {
    JsonObject find(String id);

    JsonObject findAll(Integer pageNo, Integer pageSize);

    JsonObject add(Group group);

    JsonObject findByCondition(Integer pageNo, Integer pageSize, String match);

    JsonObject update(String id, String data);

    JsonObject updateStatus(String id, Boolean valid);

    JsonObject delete(String id);
}
