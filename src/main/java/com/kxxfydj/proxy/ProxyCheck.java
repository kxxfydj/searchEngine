package com.kxxfydj.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.kxxfydj.entity.Proxy;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.ProxyService;
import com.kxxfydj.utils.ApplicationContextUtils;
import com.kxxfydj.utils.HeaderUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by kxxfydj on 2018/3/14.
 */
public class ProxyCheck {

    private static final Logger logger = LoggerFactory.getLogger(ProxyCheck.class);

    private ProxyCenter proxyCenter;

    private ProxyService proxyService;

    ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    public ProxyCheck(ProxyService proxyService ,ProxyCenter proxyCenter) {
        this.proxyCenter = proxyCenter;
        this.proxyService = proxyService;
    }

    public void checkProxiesFromDatabase(){
        List<Proxy> proxyList = proxyService.getProxiesEnabled();
        check(proxyList);
    }

    public void checkProxiesFromRedisCache(){
        List<Proxy> proxyList = proxyCenter.getAllCachedProxies();
        check(proxyList);
    }

    private void check(List<Proxy> proxyList){
        try(CloseableHttpClient httpClient = HttpClients.createDefault()
        ){
            List<Proxy> removedProxyList = new ArrayList<>();
            for(int i = 0; i < proxyList.size(); i++){
                Proxy autoProxy = proxyList.get(i);
                HttpHost httpHost = new HttpHost(autoProxy.getIp(), autoProxy.getPort());
                RequestConfig config = RequestConfig.custom().setProxy(httpHost)
                        .setConnectTimeout(5000).setSocketTimeout(5000).setConnectionRequestTimeout(5000).build();
                HttpGet get = new HttpGet("https://www.github.com");
                get.setConfig(config);
                Map<String,String> header = HeaderUtils.initGetHeaders("github.com","","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
                header.forEach((key,value) -> get.setHeader(key,value));

                ListenableFuture<Boolean> getFuture = service.submit(()->{
                    long start = System.currentTimeMillis();
                    long end;
                    try(CloseableHttpResponse response = httpClient.execute(get)){
                        end = System.currentTimeMillis();
                        autoProxy.setSpeed(end - start);
                        logger.info("代理IP：{}:{}可用",autoProxy.getIp(),autoProxy.getPort());
                        return true;
                    }catch (IOException e){
                        logger.info("代理Ip：{}:{}链接超时", autoProxy.getIp(),autoProxy.getPort());
                    }
                    return false;
                });

                try {
                    boolean result = getFuture.get(15, TimeUnit.SECONDS);
                    if(!result){
                        //remove from the redis cache and  change the enabled value then update the database
                        Proxy removedProxy = proxyList.remove(i--);
                        removedProxy.setEnabled(false);
                        removedProxyList.add(removedProxy);
                        logger.info("代理IP:{}:{}不可用，从代理池中移除", autoProxy.getIp(), autoProxy.getPort());
                    }
                }catch (Exception e){
                    logger.info("检查代理程序线程等待超时，认定为代理ip失效");
                    //remove from the redis cache and  change the enabled value then update the database
                    Proxy removedProxy = proxyList.remove(i--);
                    removedProxy.setEnabled(false);
                    removedProxyList.add(removedProxy);
                    logger.info("代理IP:{}:{}不可用，从代理池中移除", autoProxy.getIp(), autoProxy.getPort());
                }
            }

            //update the database
            int rows = proxyService.updateProxies(removedProxyList);
            logger.info("更新数据库{}条信息", rows);
            //sort the list and updata the redis cache
            Collections.sort(proxyList);
            proxyCenter.clearThenPut(proxyList);
            logger.info("更新redis proxy缓存");
        }catch (IOException e){
            logger.error(e.getMessage(),e);
        }

    }
}
