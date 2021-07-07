package com.hitszplaza.background.pojo;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import lombok.Data;

@Data
public class QueryDTO {
    private String env = WeChatAPIConstant.WX_APP_ENV;
    private String query;

    public QueryDTO(String query) {
        this.query = query;
    }
}
