package com.hitszplaza.background.pojo;

import lombok.Data;

@Data
public class Access {
    private Integer accessId;
    private String accessToken;
    private Integer expireTime;
}
