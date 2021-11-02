package com.hitszplaza.background.service;

import com.google.gson.JsonObject;
import com.hitszplaza.background.pojo.Group;
import jdk.nashorn.internal.scripts.JO;

public interface GroupApplyService {

    JsonObject find(String id);

    JsonObject findAll(Integer pageNo, Integer pageSize);

    JsonObject findByCondition(Integer pageNo, Integer pageSize, String match);

    JsonObject accept(String id, String openid, Group group, String msg);

    JsonObject decline(String id, String openid, String msg);

    JsonObject delete(String id);
}
