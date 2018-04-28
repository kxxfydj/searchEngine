package com.kxxfydj.crawler;

import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CrawlerTask;

/**
 * create by kaiming_xu on 2017/9/9
 */
public interface Crawler extends Runnable{
    void setCrawlerConfig(CrawlerConfig crawlerConfig);
    void setCrawlerTask(CrawlerTask crawlerTask);
    void crawler();
}
