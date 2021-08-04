package com.hitszplaza.background.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hitszplaza.background.mapper.SwiperMapper;
import com.hitszplaza.background.pojo.Swiper;
import com.hitszplaza.background.service.SwiperService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SwiperServiceImpl implements SwiperService {

    @Autowired
    private SwiperMapper swiperMapper;

    //TODO: swiper 中比较重要的部分是如何根据 start_time 和 end_time 自动变换 status
    //      另外就是如何在 swiper 状态为 active 时让 swiper 显示在小程序正确的位置上。

    /***
     * @description 向数据库添加 swiper
     * @param swiper swiper 实体类
     */
    @Override
    public void addSwiper(Swiper swiper) {
        if (swiper.getCurrStatus() == null) {
            swiper.setCurrStatus(0);
        }
        swiper.setStartTime(new Timestamp(new Date(swiper.getStartTime().getTime() * 1000).getTime()));
        swiper.setEndTime(new Timestamp(new Date(swiper.getEndTime().getTime() * 1000).getTime()));
        System.out.println(swiper.getStartTime());
        int is_success = swiperMapper.upload2Database(swiper);
        if (is_success == 1) {
            log.info("新增 swiper 数据成功！");
        } else {
            log.warn("未新增任何 swiper 数据！");
        }
    }

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
                System.out.println(fileName);
                System.out.println(filePath);
                return response.put("status", true).put("msg", map);
            } catch (FileNotFoundException e) {
                return response.put("status", false).put("msg", "上传文件失败 FileNotFoundException：" + e.getMessage());
            } catch (IOException e) {
                return response.put("status", false).put("msg", "上传文件失败 IOException：" + e.getMessage());
            }
        } else {
            return response.put("status", false).put("msg", "File is empty!");
        }
    }

    /***
     * @description 查询所有 swiper（已实现分页查询）
     * @param pageNo 第几页
     * @param pageSize 每页 swiper 数量
     * @return 包含当前页信息以及页位置的类，returnVal.data.list 中存储查询结果
     */
    @Override
    public PageInfo<Swiper> findAll(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return new PageInfo<>(swiperMapper.findAll());
    }

    /***
     * @description 依据状态筛选 swiper
     * @param pageNo 第几页
     * @param pageSize 每页 swiper 数量
     * @param status swiper 的状态
     * @return 包含当前页信息以及页位置的类，returnVal.data.list 中存储查询结果
     */
    @Override
    public PageInfo<Swiper> find(int pageNo, int pageSize, int status) {
        PageHelper.startPage(pageNo, pageSize);
        return new PageInfo<>(swiperMapper.find(status));
    }

    /***
     * @description 根据 swiperId 删除 swiper
     * @param swiperId swiper 表的主键
     */
    @Override
    public void delete(Integer swiperId) {
        int is_success = swiperMapper.delete(swiperId);
        if (is_success == 1) {
            log.info("swiperId = {} 的 swiper 删除成功！", swiperId);
        } else {
            log.warn("对 swiperId = {} 的删除无效！未删除任何 swiper！", swiperId);
        }
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
