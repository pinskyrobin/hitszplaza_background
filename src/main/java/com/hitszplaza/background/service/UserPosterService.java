package com.hitszplaza.background.service;

import java.util.ArrayList;
import java.util.Date;

public interface UserPosterService {
    ArrayList<String> findAll(Integer number);

    String updateOneStatus(String id, Boolean valid);

    //TODO:任务列表(优先度依次递减)
    String deleteOne(String id);

    String findAllWithOpenId(String openId);

    String findAllWithType(String type);

    String findAllWithTime(Date time);

    String findAllWithAnoymous(Integer number);

    String updateAllStatusWithOpenId(String openId);
}
