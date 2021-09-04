package com.hitszplaza.background.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class News {
    private Integer fingerprint;
    private Date releaseDate;
    private Long pullTime;
    private String title;
    private String picUrl;
    private String clickUrl;
    private Integer category;
    private Boolean status;
}
