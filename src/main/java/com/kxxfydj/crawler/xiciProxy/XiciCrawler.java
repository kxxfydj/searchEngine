package com.kxxfydj.crawler.xiciProxy;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.crawler.CrawlerBase;
import com.kxxfydj.crawler.github.GitHubProcessor;
import com.kxxfydj.crawlerConfig.CommonPipeline;
import com.kxxfydj.crawlerConfig.CrawlerListener;
import com.kxxfydj.crawlerConfig.MyHttpClientDownloader;
import com.kxxfydj.crawlerConfig.MyQueueScheduler;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import com.kxxfydj.utils.HeaderUtils;
import com.kxxfydj.utils.RequestUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.util.Map;

/**
 * Created by kxxfydj on 2018/3/16.
 */
@Crawl(crawlerName = "xici")
public class XiciCrawler extends CrawlerBase {
    @Override
    public void crawler() {
        Map<String,String> requestHeader = HeaderUtils.initGetHeaders("www.xicidaili.com","www.baidu.com","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
        requestHeader.forEach((key,value) -> site = site.addHeader(key,value));
        site = site.setTimeOut(15*1000);

        Request request = RequestUtil.createGetRequest("http://www.xicidaili.com/", CommonTag.FIRST_PAGE);

        Spider spider = Spider
                .create(new XiciProcessor(site))
                .setDownloader(new MyHttpClientDownloader(this))
                .addRequest(request)
                .setScheduler(new MyQueueScheduler())
                .thread(crawlerConfig.getSpiderThreadSize())
                .addPipeline(new CommonPipeline());

        new CrawlerListener(spider);
        spider.start();
    }
}
