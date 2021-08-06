package com.hitszplaza.background.service.impl;

import com.hitszplaza.background.pojo.Swiper;
import com.hitszplaza.background.service.SwiperService;
import com.hitszplaza.background.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class SwiperServiceImpl implements SwiperService {

    @Autowired
    private RedisUtil redisUtil;

    /***
     * @description 上传 swiper 至服务器(不包括传至数据库)
     * @param originalSwiper 图片文件流
     * @return 包含状态和文件名、文件路径的 json 格式字符串
     */
    @Override
    public JSONObject uploadSwiper(MultipartFile originalSwiper) {
        JSONObject response = new JSONObject();
        // 获取文件名与文件路径
        String fileName = originalSwiper.getOriginalFilename();
        assert fileName != null;
        fileName = getFileName(fileName);
        String filePath = getUploadPath();

        // 所传文件非空
        if (!originalSwiper.isEmpty()) {
            try (BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(filePath + File.separator + fileName))) {
                // 上传
                out.write(originalSwiper.getBytes());
                out.flush();
                Map<String, String> map = new HashMap<>();
                map.put("fileName", fileName);
                map.put("filePath", filePath);
                response.put("status", true).put("msg", map);
            } catch (FileNotFoundException e) {
                response.put("status", false).put("msg", "上传文件失败 FileNotFoundException：" + e.getMessage());
            } catch (IOException e) {
                response.put("status", false).put("msg", "上传文件失败 IOException：" + e.getMessage());
            }
        } else {
            response.put("status", false).put("msg", "File is empty!");
        }
        return response;
    }

    /***
     * @description 查找特定状态的 swiper （不支持分页）
     */
    @Override
    public List<Swiper> find(Integer currStatus) {
        Swiper swiper;
        List<Swiper> swiperList = new ArrayList<>();
        for (String key : redisUtil.hashGets("swiper_*")) {
            swiper = hashGetSwiper(redisUtil.hashGet(key));
            if (currStatus == null || currStatus.equals(swiper.getCurrStatus())) {
                swiperList.add(swiper);
            }
        }
        return swiperList;
    }

    /***
     * @description 将 swiper 数据存至数据库
     */
    @Override
    public Boolean add(Swiper swiper) {
        try {
            Integer swiperId = Integer.valueOf(redisUtil.get("swiperId"));
            Boolean success = update(swiperId, swiper);
            if (success) {
                redisUtil.update("swiperId", (++swiperId).toString());
            }
            return success;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            log.error("从 Redis 取得的 swiperId 为 String 类型，转换为 Integer 时发生格式错误！");
            return false;
        }
    }

    /***
     * @description 更新 swiper 及过期时间
     * @param swiper 一定要传完整的swiper(除 currStatus 字段)！否则未传的字段会被空值覆盖
     */
    @Override
    public Boolean update(Integer swiperId, Swiper swiper) {
        if (swiper.getCurrStatus() == null) {
            swiper.setCurrStatus(0);
        }
        String key = "swiper_" + swiperId;
        BeanMap beanMap = BeanMap.create(swiper);
        HashMap<String, String> map = new HashMap<>();
        for (Object o : beanMap.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) o;
            String _key = entry.getKey();
            Object _value = entry.getValue();
            map.put(_key, _value != null ? String.valueOf(_value) : null);
        }
        boolean success =  redisUtil.hashUpdate(key, map);
        if (success) {
            log.info("swiperId = {} 的 swiper 更新成功！", swiperId);
        } else {
            log.warn("对 swiperId = {} 的更新无效！未更新任何 swiper！", swiperId);
        }
        Long time = Long.parseLong(map.get("startTime")) - System.currentTimeMillis();
        success = redisUtil.set("PTTL_" + key, time.toString(), time);
        if (success) {
            log.info("swiperId = {} 的有效期更新成功！", swiperId);
        } else {
            log.warn("对 swiperId = {} 的有效期更新无效！未更新任何 swiper 的有效期！", swiperId);
        }
        return success;
    }

    public Boolean updateStatus(Integer swiperId, Integer nextStatus) {
        String key = "swiper_" + swiperId;
        boolean success =  redisUtil.hashSetField(key, "currStatus", nextStatus.toString());
        if (success) {
            log.info("swiperId = {} 的 swiper 更新成功！", swiperId);
        } else {
            log.warn("对 swiperId = {} 的更新无效！未更新任何 swiper！", swiperId);
        }
        return success;
    }

    @Override
    public Boolean delete(Integer _swiperId) {
        String key = "swiper_" + _swiperId;
        Boolean success = redisUtil.delete(key);
        if (success) {
            log.info("swiperId = {} 的 swiper 删除成功！", _swiperId);
        } else {
            log.warn("对 swiperId = {} 的删除无效！未删除任何 swiper！", _swiperId);
        }
        return success;
    }

    /***
     * @description  将 swiper 类型的 Map转换为实体类
     */
    public Swiper hashGetSwiper(Map<Object, Object> map) {
        Swiper swiper = new Swiper();
        swiper.setClickUrl((String) map.get("clickUrl"));
        swiper.setStorageDir((String) map.get("storageDir"));
        if (map.get("currStatus") != null) {
            swiper.setCurrStatus(Integer.valueOf(map.get("currStatus").toString()));
        }
        swiper.setInfo((String) map.get("info"));
        swiper.setStartTime(Long.valueOf(map.get("startTime").toString()));
        return swiper;
    }

    /**
     * @description 文件名后缀前添加一个时间戳
     */
    private String getFileName(String fileName) {
        int index = fileName.lastIndexOf(".");
        final SimpleDateFormat sDateFormate = new SimpleDateFormat("yyyyMMddHHmmss");  //设置时间格式
        String nowTimeStr = sDateFormate.format(new Date()); // 当前时间
        fileName = fileName.substring(0, index) + "_" + nowTimeStr + fileName.substring(index);
        return fileName;
    }

    /**
     * 获取当前系统路径
     */
    private String getUploadPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert path != null;
        if (!path.exists()) path = new File("");
        File upload = new File(path.getAbsolutePath(), "static/upload/swiper");
        if (!upload.exists()) upload.mkdirs();
        return upload.getAbsolutePath();
    }

}
