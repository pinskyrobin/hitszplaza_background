package com.hitszplaza.background.pojo;

import lombok.Data;


@Data
public class Group {
    private String name;
    private String property;
    private String intro;
    private Long createTime;
    private Integer category;
    private Boolean valid;
}
