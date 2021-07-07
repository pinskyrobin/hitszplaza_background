package com.hitszplaza.background.service;

import java.util.ArrayList;

public interface DatabaseService {
    String findOne();
    ArrayList<String> findAll();
    void updateOne();
    void deleteOne();
}
