package com.hitszplaza.background.service.impl;

import com.hitszplaza.background.constant.WeChatAPIConstant;
import com.hitszplaza.background.pojo.Swiper;
import com.hitszplaza.background.service.SwiperService;
import com.hitszplaza.background.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private Integer swiperId;

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
        String filePath = WeChatAPIConstant.STORAGE_DIR;
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 所传文件非空
        if (!originalSwiper.isEmpty()) {
            try {
                File dest = new File(filePath + fileName);
                originalSwiper.transferTo(dest);
                Map<String, String> map = new HashMap<>();
                map.put("storageDir", filePath + fileName);
                map.put("accessUrl", WeChatAPIConstant.BASE_URL + "/file/" + fileName);
                response.put("errcode", 0).put("errmsg", map);
            } catch (FileNotFoundException e) {
                response.put("errcode", -1).put("errmsg", "上传文件失败 FileNotFoundException：" + e.getMessage());
            } catch (IOException e) {
                response.put("errcode", -2).put("errmsg", "上传文件失败 IOException：" + e.getMessage());
            }
        } else {
            response.put("errcode", -3).put("errmsg", "File is empty!");
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
                swiper.setSwiperId(Integer.parseInt(key.substring(key.lastIndexOf("_") + 1)));
                swiperList.add(swiper);
            }
        }
        return swiperList;
    }

    /***
     * @description 将 swiper 数据存至数据库
     */
    @Override
    public Integer add(Swiper swiper) {
        int id = Integer.parseInt(redisUtil.get("swiperId"));
        setSwiperId(id);
        swiper.setSwiperId(id);
        Integer status = update(swiper);
        if (status == 0) {
            redisUtil.update("swiperId", (++swiperId).toString());
        } else {
            delete(id);
        }
        return status;
    }

    /***
     * @description 更新 swiper 及过期时间
     * @param swiper 一定要传完整的swiper(除 currStatus 字段)！否则未传的字段会被空值覆盖
     */
    @Override
    public Integer update(Swiper swiper) {
        if (swiper.getCurrStatus() == null) {
            swiper.setCurrStatus(0);
        }
        String key = "swiper_" + swiper.getSwiperId();
        BeanMap beanMap = BeanMap.create(swiper);
        HashMap<String, String> map = new HashMap<>();
        for (Object o : beanMap.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) o;
            String _key = entry.getKey();
            Object _value = entry.getValue();
            map.put(_key, _value != null ? String.valueOf(_value) : null);
        }
        boolean success = redisUtil.hashUpdate(key, map);
        if (success) {
            log.info("swiperId = {} 的 swiper 更新成功！", swiper.getSwiperId());
        } else {
            log.warn("对 swiperId = {} 的更新无效！未更新任何 swiper！", swiper.getSwiperId());
        }
        Long time = Long.parseLong(map.get("startTime")) - System.currentTimeMillis();
        boolean PTTLsuccess = redisUtil.set("PTTL_" + key, time.toString(), time);
        if (PTTLsuccess) {
            log.info("swiperId = {} 的有效期更新成功！", swiper.getSwiperId());
        } else {
            log.warn("对 swiperId = {} 的有效期更新无效！未更新任何 swiper 的有效期！", swiper.getSwiperId());
        }
        if (success && PTTLsuccess) {
            return 0;
        } else if (!success && PTTLsuccess) {
            return -1;
        } else if (success) {
            return -2;
        } else {
            return -3;
        }
    }

    public Boolean updateStatus(Integer swiperId, Integer nextStatus) {
        String key = "swiper_" + swiperId;
        boolean success = redisUtil.hashSetField(key, "currStatus", nextStatus.toString());
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
        String PTTLkey = "PTTL_swiper_" + _swiperId;
        redisUtil.delete(PTTLkey);
        Boolean success = redisUtil.delete(key);
        if (success) {
            log.info("swiperId = {} 的 swiper 删除成功！", _swiperId);
        } else {
            log.warn("对 swiperId = {} 的删除无效！未删除任何 swiper！", _swiperId);
        }
        return success;
    }

    /***
     * @description 将 swiper 类型的 Map转换为实体类
     */
    public Swiper hashGetSwiper(Map<Object, Object> map) {
        Swiper swiper = new Swiper();
        swiper.setClickUrl((String) map.get("clickUrl"));
        swiper.setAccessUrl((String) map.get("accessUrl"));
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

    public void setSwiperId(Integer swiperId) {
        this.swiperId = swiperId;
    }
}
