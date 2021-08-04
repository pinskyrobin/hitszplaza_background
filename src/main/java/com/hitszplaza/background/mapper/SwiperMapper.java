package com.hitszplaza.background.mapper;

import com.hitszplaza.background.pojo.Swiper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SwiperMapper {

    @Options(useGeneratedKeys = true, keyProperty = "swiperId")
    @Insert("INSERT INTO swiper(storage_dir, click_url, info, curr_status, start_time, end_time) " +
            "VALUES (#{storageDir}, #{clickUrl}, #{info}, #{currStatus}, #{startTime}, #{endTime})")
    int upload2Database(Swiper swiper);

    @Select("SELECT * FROM swiper")
    List<Swiper> findAll();

    @Select("SELECT * FROM swiper WHERE curr_status=#{currStatus}")
    List<Swiper> find(Integer currStatus);

    @Delete("DELETE FROM swiper WHERE swiper_id=#{swiperId}")
    int delete(Integer swiperId);

}
