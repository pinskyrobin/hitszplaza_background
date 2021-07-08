package com.hitszplaza.background.service;

import com.hitszplaza.background.exception.WeChatException;

public interface UserInfoService {
    String findAll(Integer number) throws WeChatException;
}
