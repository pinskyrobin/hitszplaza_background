package com.hitszplaza.background.mapper;

import com.hitszplaza.background.pojo.Lecture;
import com.hitszplaza.background.pojo.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LectureMapper {

    @Select("SELECT COUNT(*) " +
            "FROM lecture")
    int count();

    @Insert("INSERT INTO lecture(fingerprint, release_date, pull_time, title, speaker, lecture_date, lecture_day, lecture_time, location, click_url, views)" +
            "VALUES(#{fingerprint},#{releaseDate},#{pullTime},#{title},#{speaker},#{lectureDate},#{lectureDay},#{lectureTime},#{location},#{clickUrl},#{views})")
    void insert(Lecture lecture);

    @Select("SELECT * " +
            "FROM lecture " +
            "WHERE fingerprint=#{fingerprint} ")
    Lecture find(Integer id);

    @Select("SELECT * " +
            "FROM lecture " +
            "ORDER BY release_date DESC, pull_time DESC")
    List<Lecture> findAll();

    @Select("SELECT * " +
            "FROM lecture " +
            "WHERE status=#{valid} " +
            "ORDER BY release_date DESC, pull_time DESC")
    List<Lecture> findByStatus(Boolean valid);

    @Update("UPDATE lecture SET status=#{valid} WHERE fingerprint=#{id}")
    int updateStatus(Integer id, Boolean valid);

    @Delete("DELETE FROM lecture WHERE fingerprint=#{id}")
    int delete(Integer id);
}
