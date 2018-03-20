package com.kxxfydj.crawler;

import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.Proxy;
import com.kxxfydj.proxy.ProxyCenter;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.utils.JsoupRequestData;
import org.apache.http.HttpHost;
import org.springframework.web.context.ContextLoader;
import us.codecraft.webmagic.Site;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * create by kaiming_xu on 2017/9/5
 */
public abstract class CrawlerBase implements Crawler {

    protected CrawlerConfig crawlerConfig;

    protected String language;

    protected Site site = Site.me()
            .setSleepTime(0);

    protected CrawlerBase(String language){
        this.language = language;
    }

    protected CrawlerBase(){}

    protected void setProxy(){
        if (ContextLoader.getCurrentWebApplicationContext() != null) {
            ProxyCenter proxyCenter = ContextLoader.getCurrentWebApplicationContext().getBean(ProxyCenter.class);
            java.net.Proxy proxy = proxyCenter.availableProxy();
            InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
            HttpHost httpHost = new HttpHost(socketAddress.getHostName(),socketAddress.getPort());
            site.setHttpProxy(httpHost);
        }
    }

    protected void setProxy(JsoupRequestData jsoupRequestData){
        java.net.Proxy proxy = jsoupRequestData.getProxy();
        InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
        HttpHost httpHost = new HttpHost(socketAddress.getHostName(),socketAddress.getPort());
        site.setHttpProxy(httpHost);
    }

    protected void changeProxy(){
        ProxyCenter proxyCenter = ContextLoader.getCurrentWebApplicationContext().getBean(ProxyCenter.class);
        HttpHost oringinHttpHost = this.site.getHttpProxy();
        Proxy proxy = new Proxy(oringinHttpHost.getHostName(),oringinHttpHost.getPort());
        java.net.Proxy newProxy = proxyCenter.changeProxy(proxy);
        InetSocketAddress socketAddress = (InetSocketAddress) newProxy.address();
        HttpHost httpHost = new HttpHost(socketAddress.getHostName(),socketAddress.getPort());
        site.setHttpProxy(httpHost);
    }

    @Override
    public void setCrawlerConfig(CrawlerConfig crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
    }
}
