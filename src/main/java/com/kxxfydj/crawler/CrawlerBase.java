package com.kxxfydj.crawler;

import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import com.kxxfydj.entity.Proxy;
import com.kxxfydj.proxy.ProxyCenter;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.utils.ApplicationContextUtils;
import com.kxxfydj.utils.JsoupRequestData;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import us.codecraft.webmagic.Site;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * create by kaiming_xu on 2017/9/5
 */
public abstract class CrawlerBase implements Crawler {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerBase.class);

    protected CrawlerConfig crawlerConfig;

    protected String condition;

    protected Site site = Site.me()
            .setSleepTime(500);

    protected CrawlerBase(String condition){
        this.condition = condition;
    }

    protected CrawlerBase(){}

    protected void setProxy(){
        ProxyCenter proxyCenter = ApplicationContextUtils.getBean(ProxyCenter.class);
        if(proxyCenter != null){
            java.net.Proxy proxy = proxyCenter.availableProxy();
            InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
            HttpHost httpHost = new HttpHost(socketAddress.getHostName(),socketAddress.getPort());
            site.setHttpProxy(httpHost);
        }else {
            logger.warn("没有使用代理进行爬取！");
        }
    }

    protected void setProxy(JsoupRequestData jsoupRequestData){
        java.net.Proxy proxy = jsoupRequestData.getProxy();
        InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
        HttpHost httpHost = new HttpHost(socketAddress.getHostName(),socketAddress.getPort());
        site.setHttpProxy(httpHost);
    }

    public void changeProxy(){
        ProxyCenter proxyCenter = ApplicationContextUtils.getBean(ProxyCenter.class);
        HttpHost oringinHttpHost = this.site.getHttpProxy();
        Proxy proxy = new Proxy(oringinHttpHost.getHostName(),oringinHttpHost.getPort());
        java.net.Proxy newProxy = proxyCenter.changeProxy(proxy);
        InetSocketAddress socketAddress = (InetSocketAddress) newProxy.address();
        HttpHost httpHost = new HttpHost(socketAddress.getHostName(),socketAddress.getPort());
        site.setHttpProxy(httpHost);
    }

    public String getProxyIp(){
        return site.getHttpProxy().getHostName();
    }

    @Override
    public void setCrawlerConfig(CrawlerConfig crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
    }
}
