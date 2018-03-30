package com.kxxfydj.crawler.gitlab;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.crawler.CrawlerBase;
import com.kxxfydj.crawlerConfig.CommonPipeline;
import com.kxxfydj.crawlerConfig.CrawlerListener;
import com.kxxfydj.crawlerConfig.MyHttpClientDownloader;
import com.kxxfydj.crawlerConfig.MyQueueScheduler;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import com.kxxfydj.utils.RequestUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

/**
 * Created by kxxfydj on 2018/3/24.
 */
@Crawl(crawlerName = "gitlab")
public class GitLabCrawler extends CrawlerBase {

    @Override
    public void run() {
        site = site.addHeader("Referer", "https://gitlab.com/explore/projects/trending")
                .setTimeOut(15 * 1000)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")
                .addHeader("Host", "gitlab.com");

        super.setProxy();
        Request request = RequestUtil.createGetRequest("https://gitlab.com/explore/projects/starred", CommonTag.FIRST_PAGE);

        Spider spider = Spider
                .create(new GitLabProcessor(site,this.crawlerTask))
                .setDownloader(new MyHttpClientDownloader(this))
                .addRequest(request)
                .setScheduler(new MyQueueScheduler())
                .thread(crawlerConfig.getSpiderThreadSize())
                .addPipeline(new CommonPipeline());

        new CrawlerListener(spider);
        spider.start();
    }
}
