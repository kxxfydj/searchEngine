package com.kxxfydj.crawlerConfig;

import com.kxxfydj.crawler.CrawlerBase;
import com.kxxfydj.proxy.ProxyCenter;
import com.kxxfydj.service.ProxyService;
import com.kxxfydj.utils.ApplicationContextUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;

/**
 * create by kaiming_xu on 2017/9/3
 */
public class MyHttpClientDownloader extends HttpClientDownloader {

    private static Logger logger = LoggerFactory.getLogger(MyHttpClientDownloader.class);

    private MyHttpClientGenerator httpClientGenerator;

    private CrawlerBase crawlerBase;

    public MyHttpClientDownloader(CrawlerBase crawlerBase) {
        this.crawlerBase = crawlerBase;
        try {
            Field httpClientGeneratorField = HttpClientDownloader.class.getDeclaredField("httpClientGenerator");
            httpClientGeneratorField.setAccessible(true);
            httpClientGenerator = new MyHttpClientGenerator();
            httpClientGeneratorField.set(this, httpClientGenerator);
        } catch (Exception e) {
            logger.error("Init MyHttpClientGenerator occur error! ", e);
        }
    }

    @Override
    public Page download(Request request, Task task) {
        logger.info("downloading page : {}" ,request.getUrl());
        Page page = super.download(request, task);
        if(page == null){
            int retryCount = 3;
            while(retryCount -- > 0 && page == null) {
                logger.warn("原代理失效ip：{}", crawlerBase.getProxyIp());
                crawlerBase.changeProxy();
                logger.warn("切换新代理ip：{}", crawlerBase.getProxyIp());
                page =  super.download(request, task);
            }
        }
        if(page == null){
            logger.error("download page failed! url:{}",request.getUrl());
        }
        return page;
    }
}
