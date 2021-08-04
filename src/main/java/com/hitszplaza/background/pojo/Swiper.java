package com.hitszplaza.background.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Swiper {
    private Integer swiperId;
    private String clickUrl;
    private String storageDir;
    private String info;
    private Integer currStatus;
    private Timestamp startTime;
    private Timestamp endTime;
}
