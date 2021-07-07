package com.hitszplaza.background.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class Poster {
    private String _id;
    private String clickUrl;
    private String imgUrl;
    private ArrayList<String> tags;
    private Date time;
    private Date time_show;
    private String title;
}
