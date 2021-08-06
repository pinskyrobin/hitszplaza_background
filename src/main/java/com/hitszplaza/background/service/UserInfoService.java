package com.hitszplaza.background.service;

public interface UserInfoService {
    String find(String openId);

    String findAll(Integer pageNo, Integer pageSize);
}
