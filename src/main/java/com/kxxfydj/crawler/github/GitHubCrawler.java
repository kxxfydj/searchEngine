package com.kxxfydj.crawler.github;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.crawler.CrawlerBase;
import com.kxxfydj.crawlerConfig.CommonPipeline;
import com.kxxfydj.crawlerConfig.CrawlerListener;
import com.kxxfydj.crawlerConfig.MyHttpClientDownloader;
import com.kxxfydj.crawlerConfig.MyQueueScheduler;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import com.kxxfydj.utils.RequestUtil;
import com.kxxfydj.utils.URLUtil;
import org.apache.http.client.utils.URLEncodedUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.net.URLEncoder;

/**
 * create by kaiming_xu on 2017/9/2
 */
@Crawl(crawlerName = "github")
public class GitHubCrawler extends CrawlerBase {

    @Override
    public void crawler() {

        site = site.addHeader("Referer", "https://github.com/")
                .addHeader("Host", "github.com");

        super.setProxy();
        super.setFiddlerProxy();
        String url = URLUtil.encode("https://github.com/search?utf8=%E2%9C%93&q=" + this.condition + "&type=","UTF-8");
        url = url.replaceAll("%2B","+");
        Request request = RequestUtil.createGetRequest(url, CommonTag.FIRST_PAGE);

        Spider spider = Spider
                .create(new GitHubProcessor(site, this.crawlerTask))
                .setDownloader(new MyHttpClientDownloader(this))
                .addRequest(request)
                .setScheduler(new MyQueueScheduler())
                .thread(crawlerConfig.getSpiderThreadSize())
                .addPipeline(new CommonPipeline(this.crawlerTask,crawlerConfig));

        new CrawlerListener(spider);
        spider.start();
    }

}
