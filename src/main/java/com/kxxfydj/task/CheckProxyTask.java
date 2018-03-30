package com.kxxfydj.task;

import com.kxxfydj.proxy.ProxyCenter;
import com.kxxfydj.proxy.ProxyCheck;
import com.kxxfydj.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by kxxfydj on 2018/3/25.
 */
@Component
public class CheckProxyTask {

    @Autowired
    ProxyService proxyService;

    @Autowired
    ProxyCenter proxyCenter;

    @Scheduled(cron = "0 0 /6 * * ?")
    public void checkDatabase() {
        ProxyCheck proxyCheck = new ProxyCheck(proxyService, proxyCenter);
        proxyCheck.checkProxiesFromDatabase();
    }

    @Scheduled(cron = "0 0 /2 * * ?")
    public void checkRedis(){
        ProxyCheck proxyCheck = new ProxyCheck(proxyService, proxyCenter);
        proxyCheck.checkProxiesFromRedisCache();
    }
}
