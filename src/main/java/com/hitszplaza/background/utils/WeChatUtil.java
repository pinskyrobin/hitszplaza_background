package com.hitszplaza.background.utils;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.pojo.Access;
import com.hitszplaza.background.pojo.QueryDTO;
import exception.WeChatException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WeChatUtil {

    @Autowired
    private RestTemplate restTemplate;

    // 获取access_token的接口地址, 限2000（次/天）
    public Access getToken() {
        Access token;
        String url = WeChatAPIConstant.WX_API_HOST +
                "/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";
        Map<String, String> params = new HashMap<>();
        params.put("APPID", WeChatAPIConstant.WX_APPID);
        params.put("APPSECRET", WeChatAPIConstant.WX_APPSECRET);
        JSONObject json = new JSONObject(restTemplate.getForObject(url, String.class, params));

        // 将获取的access_token和expires_in放入accessToken对象中
        try{
            token = new Access();
            token.setAccessId(1);
            token.setAccessToken(json.getString("access_token"));
            token.setExpireTime(json.getInt("expires_in"));
        }
        catch (Exception e){
            token = null;
            e.printStackTrace();
            log.error("系统出错了！");
        }
        return token;
    }

    public String post(String url, String query) {
        String response = null;
        QueryDTO queryDTO = new QueryDTO(query);
        try{
            response = restTemplate.postForObject(url, queryDTO, String.class);
            JSONObject json = new JSONObject(response);
            Integer errcode = (Integer) json.get("errcode");
            if (errcode != 0) {
                throw new WeChatException((String) json.get("errmsg"));
            }
        } catch (WeChatException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(response);
            log.error("微信API调用出现其他错误!");
        }
        return response;
    }
}
