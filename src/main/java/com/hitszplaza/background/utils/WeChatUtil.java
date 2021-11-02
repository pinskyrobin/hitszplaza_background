package com.hitszplaza.background.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.pojo.Access;
import com.hitszplaza.background.pojo.QueryDTO;
import com.hitszplaza.background.exception.WeChatException;
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
     * @return Acess的pojo对象, 含accessId(仅做主键用), accessToken及过期时间expireTime(通常为7200s)
     * @description 获取access_token的接口地址, 限2000(次/天)
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

    /***
     * @description 分页查询数据
     * @param pageNo 页数（从0开始）
     * @param pageSize 页大小
     * @param preQuery 查询语句前半部分
     * @return String 格式的 json 串，包含 errcode、errmsg、pager 和 data 四个属性，
     *          pager 存储分页信息，data 存储数据。
     */
    public String pageQuery(Integer pageNo, Integer pageSize, String preQuery) {
        // 获取最新的access_token
        updateAccessToken();

        // 请求地址
        String url = WeChatAPIConstant.WX_API_HOST + "/tcb/databasequery?access_token=";

        String query = preQuery + String.format(".limit(%d).skip(%d).get()"
                , pageSize, pageNo * pageSize);

        String response = post(url, query);
        return response;
    }

    /***
     * @description 通用的更新请求模板
     * @method PATCH
     */

    public JsonObject update(String url, String query) {
        String response = post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("matched").getAsInt() == 0) {
            log.warn("未匹配到任何记录");
        } else if (json.get("modified").getAsInt() == 0) {
            log.warn("匹配到记录，但未修改任何记录");
        }
        return json;
    }

    /***
     * @description 通用的删除请求模板
     * @method DELETE
     */

    public JsonObject delete(String url, String query) {
        String response = post(url, query);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        if (json.get("deleted").getAsInt() == 0) {
            log.warn("未删除任何反馈!");
        }
        return json;
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

    public JsonObject toJson(JsonElement source) {
        String origin = source.toString();
        StringBuilder res = new StringBuilder();
        char ch;
        for (int i = 1; i < origin.length() - 1; i++) {
            ch = origin.charAt(i);
            if (ch != '\\') {
                res.append(origin.charAt(i));
            }
        }
        return new Gson().fromJson(res.toString(), JsonObject.class);
    }
}
