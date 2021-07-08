package com.hitszplaza.background.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class UserPoster {
    private String _id;
    private ArrayList<String> insertImageUrl;
    private ArrayList<String> likeList;
    private Boolean isAnoymous;
    private String openid;
    private String text;
    private Date time;
    private String type;
    private Boolean valid;
}
