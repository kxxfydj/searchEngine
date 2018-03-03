package com.kxxfydj.crawler;

import com.kxxfydj.crawlerConfig.CrawlerConfig;
import us.codecraft.webmagic.Site;

/**
 * create by kaiming_xu on 2017/9/5
 */
public abstract class CrawlerBase implements Crawler {

    protected CrawlerConfig crawlerConfig;

    protected Site site = Site.me().
            setDomain("github.com")
            .setSleepTime(0);

    @Override
    public void setCrawlerConfig(CrawlerConfig crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
    }
}
