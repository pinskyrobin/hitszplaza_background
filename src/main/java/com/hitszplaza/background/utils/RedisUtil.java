package com.hitszplaza.background.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * @description 读取缓存
     */
    public String get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @description 写入缓存
     */
    public boolean set(final String key, String value, Long time) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @description 更新缓存
     */
    public boolean update(final String key, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().getAndSet(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @description 删除缓存
     */
    public boolean delete(final String key) {
        boolean result = false;
        try {
            redisTemplate.delete(key);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<Object, Object> hashGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public String hashGetField(String key, String field) {
        Object res = redisTemplate.opsForHash().get(key, field);
        return (res == null) ? null : res.toString();
    }

    public Boolean hashSetField(String key, String field, String val) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().put(key, field,val);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @description 更新一个 hash 类型数据
     */
    public boolean hashUpdate(String key, HashMap<String, String> map) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, map);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Set<String> hashGets(String pattern) {
        Set<String > result = new HashSet<>();
        try {
            result = redisTemplate.keys(pattern);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
