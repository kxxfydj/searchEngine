package com.kxxfydj.proxy;

import com.kxxfydj.entity.Proxy;
import com.kxxfydj.redis.RedisUtil;
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
import java.util.List;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/3/14.
 */
public class ProxyCheck {

    private static final Logger logger = LoggerFactory.getLogger(ProxyCheck.class);

    private RedisUtil<String,Proxy> redisUtil;

    public void check(List<Proxy> autoProxyList){
        try(CloseableHttpClient httpClient = HttpClients.createDefault()
        ){
            for(int i = 0; i < autoProxyList.size(); i++){
                Proxy autoProxy = autoProxyList.get(i);
                HttpHost httpHost = new HttpHost(autoProxy.getIp(), autoProxy.getPort());
                RequestConfig config = RequestConfig.custom().setProxy(httpHost)
                        .setConnectTimeout(5000).setSocketTimeout(5000).build();
                HttpGet get = new HttpGet("https://www.baidu.com");
                get.setConfig(config);
                Map<String,String> header = HeaderUtils.initGetHeaders("www.baidu.com","","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
                header.forEach((key,value) -> get.setHeader(key,value));

                long start = System.currentTimeMillis();
                long end;
                try(CloseableHttpResponse response = httpClient.execute(get)){
                    end = System.currentTimeMillis();
                    autoProxy.setSpeed(start - end);
                }catch (IOException e){
                    autoProxyList.remove(i--);
                    logger.info("代理IP:{}:{}不可用，从代理池中移除", autoProxy.getIp(), autoProxy.getPort());
                    logger.error("代理池链接异常！",e.getMessage(),e);
                }
            }

            redisUtil = ApplicationContextUtils.getBean(RedisUtil.class);
            redisUtil.lSet("proxyList",autoProxyList);
        }catch (IOException e){
            logger.error(e.getMessage(),e);
        }

    }
}
