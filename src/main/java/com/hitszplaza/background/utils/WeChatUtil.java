package com.hitszplaza.background.utils;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.pojo.Access;
import com.hitszplaza.background.pojo.QueryDTO;
import com.hitszplaza.background.exception.WeChatException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WeChatUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtil redisUtil;

    private String accessToken;

    public void updateAccessToken() {
        this.accessToken = redisUtil.get("accessToken");
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * @description 获取access_token的接口地址, 限2000(次/天)
     * @return Acess的pojo对象,含accessId(仅做主键用),accessToken及过期时间expireTime(通常为7200s)
     **/
    public Access getToken() {
        Access token;
        String url = WeChatAPIConstant.WX_API_HOST +
                "/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";
        Map<String, String> params = new HashMap<>();
        params.put("APPID", WeChatAPIConstant.WX_APPID);
        params.put("APPSECRET", WeChatAPIConstant.WX_APPSECRET);
        JSONObject json = new JSONObject(restTemplate.getForObject(url, String.class, params));

        // 将获取的access_token和expires_in放入accessToken对象中
        try {
            token = new Access();
            token.setAccessId(1);
            token.setAccessToken(json.getString("access_token"));
            token.setExpireTime(json.getInt("expires_in"));
        } catch (Exception e) {
            token = null;
            e.printStackTrace();
            log.error("系统出错了！");
        }
        return token;
    }

    /**
     * @description 分批获取集合记录
     * @param number: 每批记录的数量
     * @param database: 记录所属集合
     * @param preQuery: 查询语句前部分
     * @return List/<Object/>形式的jsonArray数组,每一个元素为查询所得记录
     **/
    public List<Object> queryBatch(Integer number, String database, String preQuery) {
        // 获取最新的access_token
        updateAccessToken();

        // 获取集合的记录总数
        String countUrl = WeChatAPIConstant.WX_API_HOST +
                "/tcb/databasecount?access_token=";
        String countQuery = String.format("db.collection(\"%s\").count()", database);
        String countResponse = post(countUrl, countQuery);
        int count = (Integer) new JSONObject(countResponse).get("count");

        // 默认每次查询20条
        if (number == null) {
            number = 20;
        }
        // 查询轮数
        int batchTimes = (int) Math.ceil((double) count / number);
        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST +
                    "/tcb/databasequery?access_token=";

        int skipCount;
        String raw_response;
        JSONArray jsonResponse = new JSONArray();

        for (int i = 0; i < batchTimes; i++) {
            skipCount = i * number;
            String query = preQuery + String.format(".limit(%d).skip(%d).get()"
                    , number, skipCount);
            raw_response = post(url, query);
            jsonResponse.putAll(new JSONObject(raw_response).getJSONArray("data"));
        }
        return jsonResponse.toList();
    }
    /**
     * @description 通用微信请求模板
     * @method POST
     **/
    public String post(String url, String query) {
        // 获取最新的access_token
        updateAccessToken();
        // 为 url 添加access_token
        url = url.concat(getAccessToken());
        String response = null;
        QueryDTO queryDTO = new QueryDTO(query);
        try {
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
