package com.hitszplaza.background.mapper;

import com.hitszplaza.background.pojo.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsMapper {

    @Select("SELECT COUNT(*) " +
            "FROM news")
    int countAll();

    @Select("SELECT COUNT(*) " +
            "FROM news " +
            "WHERE category=#{category}")
    int count(Integer category);

    @Insert("INSERT INTO news(fingerprint, release_date, pull_time, title, pic_url, click_url, category, views, content)" +
            "VALUES(#{fingerprint},#{releaseDate},#{pullTime},#{title},#{picUrl},#{clickUrl},#{category},#{views},#{content})")
    void insert(News news);

    @Select("SELECT * " +
            "FROM news " +
            "WHERE category=#{category} " +
            "ORDER BY release_date DESC, pull_time DESC")
    List<News> find(Integer category);

    @Select("SELECT * " +
            "FROM news " +
            "ORDER BY release_date DESC, pull_time DESC")
    List<News> findAll();

    @Select("SELECT * " +
            "FROM news " +
            "WHERE category=#{category} AND status=#{valid} " +
            "ORDER BY release_date DESC, pull_time DESC")
    List<News> findByStatus(Integer category, Boolean valid);

    @Select("SELECT * " +
            "FROM news " +
            "WHERE status=#{valid} " +
            "ORDER BY release_date DESC, pull_time DESC")
    List<News> findAllByStatus(Boolean valid);

    @Update("UPDATE news SET status=#{valid} WHERE fingerprint=#{id}")
    int updateStatus(Integer id, Boolean valid);

    @Delete("DELETE FROM news WHERE fingerprint=#{id}")
    int delete(Integer id);

    @Select("SELECT category FROM news WHERE fingerprint=#{id}")
    int getCategory(Integer id);
}
