package com.hitszplaza.background.service;

public interface UserPosterService {
    String findAll(Integer number);
    String updateStatus(String id, Boolean valid);
}
