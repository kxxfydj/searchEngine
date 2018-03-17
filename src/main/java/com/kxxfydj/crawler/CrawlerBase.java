package com.kxxfydj.crawler;

import com.kxxfydj.crawlerConfig.CrawlerConfig;
import us.codecraft.webmagic.Site;

/**
 * create by kaiming_xu on 2017/9/5
 */
public abstract class CrawlerBase implements Crawler {

    protected CrawlerConfig crawlerConfig;

    protected String language;

    protected Site site = Site.me()
            .setSleepTime(0);

    protected CrawlerBase(String language){
        this.language = language;
    }

    protected CrawlerBase(){}

    @Override
    public void setCrawlerConfig(CrawlerConfig crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
    }
}
