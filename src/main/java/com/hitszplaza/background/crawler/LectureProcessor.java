package com.hitszplaza.background.crawler;

import com.hitszplaza.background.constant.CrawlConstant;
import com.hitszplaza.background.pojo.Lecture;
import com.hitszplaza.background.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class LectureProcessor implements PageProcessor {

    private Site site = Site.me()
            .setDomain("hitsz.edu.cn")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78");

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(CrawlConstant.LECTURE_URL).match()) {
            List<Selectable> list = page.getHtml().xpath("//div[@class='wrapper wrapper_news']/div[@class='container_news']/div[@class='mainwidth']/div[@class='mainside_news']/ul/li").nodes();
            String title, speaker, lectureDate, lectureDay, lectureTime, location, clickUrl, oringinalReleaseDate, views;
            DateFormat dateFormat;
            for (Selectable s : list) {
                title = s.xpath("//div/div[@class='lecture_top']/a/text()").toString();
                lectureDate = s.xpath("//div/div[@class='lecture_top']/span/text()").toString();
                lectureDay = s.xpath("//div/div[@class='lecture_top']/span/span/text()").toString();
                speaker = s.xpath("//div/div[@class='lecture_bottom']/div[1]/text()").toString();
                lectureTime = s.xpath("//div/div[@class='lecture_bottom']/div[2]/text()").toString();
                location = s.xpath("//div/div[@class='lecture_bottom']/div[3]/text()").toString();
                clickUrl = CrawlConstant.BASE_URL + s.xpath("//div/div[@class='lecture_top']/a/@href").toString();
                oringinalReleaseDate = s.xpath("//div/div[@class='time_t']/span[@class='date_t']/text()").toString();
                views = s.xpath("//div/div[@class='time_t']/span[@class='view']/text()").toString();
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date releaseDate = null;
                try {
                    releaseDate = dateFormat.parse(oringinalReleaseDate);
                } catch (ParseException e) {
                    log.error(e.getMessage());
                }
                Long pullTime = new Date().getTime();
                Integer fingerprint = title.hashCode();
                if (!redisUtil.hasValueInSet("fingerprint_lecture", String.valueOf(fingerprint))) {
                    redisUtil.addSet("fingerprint_lecture", String.valueOf(fingerprint));
                    Lecture lecture = new Lecture();
                    lecture.setFingerprint(fingerprint);
                    lecture.setReleaseDate(releaseDate);
                    lecture.setPullTime(pullTime);
                    lecture.setTitle(title);
                    lecture.setSpeaker(speaker);
                    lecture.setLectureDate(lectureDate);
                    lecture.setLectureDay(lectureDay);
                    lecture.setLectureTime(lectureTime);
                    lecture.setLocation(location);
                    lecture.setViews(views);
                    lecture.setClickUrl(clickUrl);
                    page.putField("lecture_" + fingerprint, lecture);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
