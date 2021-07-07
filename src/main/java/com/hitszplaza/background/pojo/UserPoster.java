package com.hitszplaza.background.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class UserPoster {
    private String _id;
    private String insertImageUrl;
    private Boolean isAnoymous;
    private String openid;
    private String text;
    private Date time;
    private String type;
    private Boolean valid;
}
