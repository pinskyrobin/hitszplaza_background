package com.hitszplaza.background.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Lecture {
    // 元组基本信息
    private Integer fingerprint;
    private Date releaseDate;
    private Long pullTime;
    private Boolean status;

    // 讲座主体信息
    private String title;
    private String speaker;
    private String lectureDate;
    private String lectureDay;
    private String lectureTime;
    private String location;
    private String views;

    // 跳转链接
    private String clickUrl;
}
