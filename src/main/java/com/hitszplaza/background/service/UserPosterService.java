package com.hitszplaza.background.service;

public interface UserPosterService {
    String findAll(Integer pageNo, Integer pageSize);

    String updateOneStatus(String id, Boolean valid);

    String updateAllStatusWithOpenId(String openId, Boolean valid);

    String deleteOne(String id);

    String findAllWithOpenId(Integer pageNo, Integer pageSize, String openId);

    String findAllWithType(Integer pageNo, Integer pageSize, String type);

    String findAllWithTime(Integer pageNo, Integer pageSize, Long startTime, Long endTime);

    String findAllWithAnoymous(Integer pageNo, Integer pageSize);
}
