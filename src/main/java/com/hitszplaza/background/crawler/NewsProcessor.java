package com.hitszplaza.background.crawler;

import com.hitszplaza.background.constant.CrawlConstant;
import com.hitszplaza.background.pojo.News;
import com.hitszplaza.background.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        if (page.getUrl().regex(CrawlConstant.NEWS_URL).match()) {
            int category = Integer.parseInt(page.getHtml().xpath("//div[@class='wrapper wrapper_news']/div[@class='header']/div[@class='mainwidth']/div/div[@class='rightside']/div/form/input[@name='pageId']/@value").toString());
            List<Selectable> list = page.getHtml().xpath("//div[@class='wrapper wrapper_news']/div[@class='container_news']/div[@class='mainwidth']/div[@class='mainside_news']/ul/li").nodes();
            String title, picUrl, clickUrl, oringinalReleaseDate, views;
            DateFormat dateFormat;
            for (Selectable s : list) {
                switch (category) {
                    case 116:
                    case 75:
                    case 77:
                    case 124:
                        title = s.xpath("//div/a/text()").toString();
                        picUrl = s.xpath("//a/img/@src").toString();
                        clickUrl = CrawlConstant.BASE_URL + s.xpath("//div/a/@href").toString();
                        oringinalReleaseDate = s.xpath("//div/span[@class='date']/text()").toString();
                        views = s.xpath("//div/span[@class='view']/text()").toString();
                        break;
                    case 80:
                    case 81:
                    case 74:
                        title = (category == 74 || category == 81) ? s.xpath("//a/text()").toString() :
                                s.xpath("//a/span/text()").toString() + s.xpath("//a/text()").toString();
                        picUrl = null;
                        clickUrl = CrawlConstant.BASE_URL + s.xpath("//a/@href").toString();
                        oringinalReleaseDate = s.xpath("//span[@class='date']/text()").toString();
                        views = s.xpath("//span[@class='view']/text()").toString();
                        break;
//                    case 78:
//                        title = s.xpath("//div/div[@class='lecture_top']/a/text()").toString();
//                        picUrl = null;
//                        clickUrl = CrawlConstant.BASE_URL + s.xpath("//div/div[@class='lecture_top']/a/@href").toString();
//                        int year = Calendar.getInstance().get(Calendar.YEAR);
//                        oringinalReleaseDate = year + "-" + s.xpath("//div/div[@class='lecture_top']/span/text()").toString().substring(0, 2) + "-" +
//                                s.xpath("//div/div[@class='lecture_top']/span/span/text()").toString().substring(0, 2);
//                        break;
                    default:
                        return;
                }
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
                    news.setViews(views);
                    page.addTargetRequest(new Request(clickUrl).putExtra("news", news));
                }
            }
        } else if (page.getUrl().regex(CrawlConstant.NEWS_CONTENT_URL).match()){
            News news = page.getRequest().getExtra("news");
            String content = page.getHtml().xpath("//div[@class='wrapper wrapper_news']/div[@class='container_news']/div/div[@class='leftside_news']/div/div[@class='detail']/div").toString();
            news.setContent(content);
            page.putField("news_" + news.getFingerprint(), news);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}