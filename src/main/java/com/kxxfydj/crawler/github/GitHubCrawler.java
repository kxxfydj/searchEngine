package com.kxxfydj.crawler.github;

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
 * create by kaiming_xu on 2017/9/2
 */
@Crawl(crawlerName = "github")
public class GitHubCrawler extends CrawlerBase {

    @Override
    public void run() {

        site = site.addHeader("Referer", "https://github.com/")
                .setTimeOut(15 * 1000)
                .addHeader("Host", "github.com");

        super.setProxy();
        Request request = RequestUtil.createGetRequest("https://github.com/search?utf8=%E2%9C%93&q=" + this.condition + "&type=", CommonTag.FIRST_PAGE);

        Spider spider = Spider
                .create(new GitHubProcessor(site, this.crawlerTask))
                .setDownloader(new MyHttpClientDownloader(this))
                .addRequest(request)
                .setScheduler(new MyQueueScheduler())
                .thread(crawlerConfig.getSpiderThreadSize())
                .addPipeline(new CommonPipeline());

        new CrawlerListener(spider);
        spider.start();
    }

}
