package com.kxxfydj.crawler.ziruwang;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.crawler.CrawlerBase;
import com.kxxfydj.crawlerConfig.CommonPipeline;
import com.kxxfydj.crawlerConfig.CrawlerListener;
import com.kxxfydj.crawlerConfig.MyHttpDownloader;
import com.kxxfydj.crawlerConfig.MyQueueScheduler;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

/**
 * create by kaiming_xu on 2017/9/2
 */
@Crawl
public class GitHubCrawler extends CrawlerBase{

    @Override
    public void run() {
        Request request = new Request("https://github.com/search?utf8=%E2%9C%93&q=java&type=");
        request.putExtra(CommonTag.TYPE,CommonTag.HOME_PAGE);

        Spider spider = Spider
                .create(new GitHubProcessor(site))
                .setDownloader(new MyHttpDownloader())
                .addRequest(request)
                .setScheduler(new MyQueueScheduler())
                .thread(crawlerConfig.getSpiderThreadSize())
                .addPipeline(new CommonPipeline());

        new CrawlerListener(spider);
        spider.start();

    }

}
