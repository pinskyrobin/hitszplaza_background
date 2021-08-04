package com.hitszplaza.background.service;

import com.github.pagehelper.PageInfo;
import com.hitszplaza.background.pojo.Swiper;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface SwiperService {
    JSONObject uploadSwiper(MultipartFile originalSwiper);
    void addSwiper(Swiper swiper);
    PageInfo<Swiper> findAll(int pageNo, int pageSize);
    PageInfo<Swiper> find(int pageNo, int pageSize, int status);
    void delete(Integer swiperId);
}
