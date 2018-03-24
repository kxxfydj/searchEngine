package com.kxxfydj.crawler.github;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.crawler.CrawlerBase;
import com.kxxfydj.crawlerConfig.CommonPipeline;
import com.kxxfydj.crawlerConfig.CrawlerListener;
import com.kxxfydj.crawlerConfig.MyHttpClientDownloader;
import com.kxxfydj.crawlerConfig.MyQueueScheduler;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import com.kxxfydj.utils.*;
import org.apache.http.HttpHost;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

/**
 * create by kaiming_xu on 2017/9/2
 */
//@Crawl
public class GitHubCrawler extends CrawlerBase{

    public GitHubCrawler(String language) {
        super(language);
    }

    public GitHubCrawler(){
        super();
    }

    @Override
    public void run() {

        site = site.addHeader("Referer","https://github.com/")
                .setTimeOut(15*1000)
                .setDomain("github.com")
                .addHeader("Content-type", "application/x-www-form-urlencoded")
                .addHeader("Host","github.com");
//        HttpHost httpHost = new HttpHost("127.0.0.1",8888);

        super.setProxy();
        Request request = RequestUtil.createGetRequest("https://github.com/search?utf8=%E2%9C%93&q=" + this.language + "&type=",CommonTag.FIRST_PAGE);

        Spider spider = Spider
                .create(new GitHubProcessor(site,this.language))
                .setDownloader(new MyHttpClientDownloader(this))
                .addRequest(request)
                .setScheduler(new MyQueueScheduler())
                .thread(crawlerConfig.getSpiderThreadSize())
                .addPipeline(new CommonPipeline());

        new CrawlerListener(spider);
        spider.start();
    }

}
