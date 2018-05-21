package com.kxxfydj.crawler.githubUpdate;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.crawler.CrawlerBase;
import com.kxxfydj.crawlerConfig.CommonPipeline;
import com.kxxfydj.crawlerConfig.CrawlerListener;
import com.kxxfydj.crawlerConfig.MyHttpClientDownloader;
import com.kxxfydj.crawlerConfig.MyQueueScheduler;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.service.CodeInfoService;
import com.kxxfydj.utils.ApplicationContextUtils;
import com.kxxfydj.utils.RequestUtil;
import com.kxxfydj.utils.URLUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.List;

/**
 * Created by kxxfydj on 2018/5/12.
 */
@Crawl(crawlerName = "githubUpdate")
public class GithubCrawler extends CrawlerBase {
    @Override
    public void crawler() {
        site = site.addHeader("Referer", "https://github.com/")
                .addHeader("Host", "github.com");

        CodeInfoService codeInfoService = ApplicationContextUtils.getBean(CodeInfoService.class);
        List<CodeInfo> codeInfoList = codeInfoService.getCodeInfoByRepository("github");

        Request[] requests = new Request[codeInfoList.size()];

        for(int i = 0; i < codeInfoList.size(); i++){
            CodeInfo codeInfo = codeInfoList.get(i);
            String author = codeInfo.getAuthor();
            String projectName = codeInfo.getProjectName();
            String url = "https://github.com/" + author + "/" + projectName;
            url = URLUtil.encode(url,"UTF-8");
            requests[i] = RequestUtil.createGetRequest(url, CommonTag.FIRST_PAGE);
        }

//        super.setProxy();
//        super.setFiddlerProxy();

        Spider spider = Spider
                .create(new GithubProcessor(site,codeInfoList,crawlerConfig,crawlerTask))
                .setDownloader(new MyHttpClientDownloader(this))
                .addRequest(requests)
                .setScheduler(new MyQueueScheduler())
                .thread(crawlerConfig.getSpiderThreadSize())
                .addPipeline(new CommonPipeline(this.crawlerTask,crawlerConfig));

        new CrawlerListener(spider);
        spider.start();
    }
}
