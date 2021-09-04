package com.hitszplaza.background.crawler;

import com.hitszplaza.background.constant.NewsConstant;
import com.hitszplaza.background.pojo.News;
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
public class NewsProcessor implements PageProcessor {

    private Site site = Site.me()
            .setDomain("hitsz.edu.cn")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78");

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(NewsConstant.TARGET_URL).match()) {
            Integer category = Integer.parseInt(page.getHtml().xpath("//div[@class='wrapper wrapper_news']/div[@class='header']/div[@class='mainwidth']/div/div[@class='rightside']/div/form/input[@name='pageId']/@value").toString());
            List<Selectable> list = page.getHtml().xpath("//div[@class='wrapper wrapper_news']/div[@class='container_news']/div[@class='mainwidth']/div[@class='mainside_news']/ul[@class='newsletters']/li").nodes();
            for (Selectable s : list) {
                String title = s.xpath("//div/a/text()").toString();
                String picUrl = s.xpath("//a/img/@src").toString();
                String clickUrl = NewsConstant.BASE_URL + s.xpath("//div/a/@href").toString();
                String oringinalReleaseDate = s.xpath("//div/span[@class='date']/text()").toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date releaseDate = null;
                try {
                    releaseDate = dateFormat.parse(oringinalReleaseDate);
                } catch (ParseException e) {
                    log.error(e.getMessage());
                }
                Long pullTime = new Date().getTime();
                Integer fingerprint = title.hashCode();
                if (!redisUtil.hasValueInSet("fingerprint_news_" + category, String.valueOf(fingerprint))) {
                    redisUtil.addSet("fingerprint_news_" + category, String.valueOf(fingerprint));
                    News news = new News();
                    news.setFingerprint(fingerprint);
                    news.setReleaseDate(releaseDate);
                    news.setPullTime(pullTime);
                    news.setTitle(title);
                    news.setPicUrl(picUrl);
                    news.setClickUrl(clickUrl);
                    news.setCategory(category);
                    page.putField("news_" + fingerprint, news);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}