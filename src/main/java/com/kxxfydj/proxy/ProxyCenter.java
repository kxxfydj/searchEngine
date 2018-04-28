package com.kxxfydj.proxy;

import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.entity.Proxy;
import com.kxxfydj.mapper.ProxyMapper;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.ProxyService;
import com.kxxfydj.task.CheckProxyTask;
import com.kxxfydj.task.CrawlerProxyTask;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import java.net.InetSocketAddress;
import java.util.*;

@Component
@Lazy(false)
public class ProxyCenter {

    private static Map<String, java.net.Proxy.Type> typeMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ProxyCenter.class);

    private static List<Proxy> usedProxyList = new ArrayList<>();

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private CrawlerProxyTask crawlerProxyTask;

    @Autowired
    private CheckProxyTask checkProxyTask;

    static {
        typeMap.put("http", java.net.Proxy.Type.HTTP);
        typeMap.put("https", java.net.Proxy.Type.HTTP);
        typeMap.put("socks4/5", java.net.Proxy.Type.SOCKS);
    }

    public synchronized List<Proxy> getAllCachedProxies(){
        return redisUtil.lGet(RedisKeys.PROXYLIST.getKey(),0,-1);
    }

    public synchronized java.net.Proxy availableProxy() {
        Proxy targetProxy = redisUtil.lLeftPop(RedisKeys.PROXYLIST.getKey());

        if(targetProxy == null){
            logger.info("redis 代理池无可用代理！开始爬取西刺代理，并检测可用性！");
            crawlerProxyTask.crawlerProxy();
            checkProxyTask.checkDatabase();
            return null;
        }

        //put the target proxy to the usedProxyList
        usedProxyList.add(targetProxy);

        InetSocketAddress socketAddress = new InetSocketAddress(targetProxy.getIp(), targetProxy.getPort());

        targetProxy.setUsedTimes(targetProxy.getUsedTimes() + 1);
        proxyService.updateProxy(targetProxy);

        return new java.net.Proxy(typeMap.get(targetProxy.getType()),socketAddress);
    }

    /**
     * put all of the proxy to the redis cache, but if the element is included in the usedProxyList will not
     * @param proxyList
     */
    public synchronized void putAllProxies(List<Proxy> proxyList){
        proxyList.removeAll(usedProxyList);
        redisUtil.lSet(RedisKeys.PROXYLIST.getKey(),proxyList);
    }

    public synchronized void clearProxyCache(){
        redisUtil.del(RedisKeys.PROXYLIST.getKey());
    }

    public synchronized void clearThenPut(List<Proxy> proxyList){
        clearProxyCache();
        putAllProxies(proxyList);
    }

    public synchronized java.net.Proxy changeProxy(Proxy originProxy){
        deleteProxy(originProxy);
        return availableProxy();
    }

    public synchronized boolean deleteProxy(Proxy originProxy){
        int index = usedProxyList.indexOf(originProxy);
        if(index == -1){
            logger.error("切换代理时发现原代理不在使用列表中！  ip:{}",originProxy.getIp());
            return false;
        }
        Proxy removedProxy = usedProxyList.remove(index);
        removedProxy.setEnabled(false);

        //update the removed proxy disabled
        proxyService.updateProxy(removedProxy);

        return true;
    }

    public synchronized void deleteProxies(List<Proxy> proxyList){
        List<Proxy> removedList = new ArrayList<>();
        for(Proxy proxy : proxyList){
            if(usedProxyList.contains(proxy)){
                logger.warn("代理ip正在使用中，不能删除！ ip:{}", proxy.getIp());
                continue;
            }
            proxy.setEnabled(false);
            removedList.add(proxy);
            redisUtil.lRemove(RedisKeys.PROXYLIST.getKey(),0,proxy);
        }
        //update the database
        proxyService.updateProxies(removedList);
        removedList.clear();
    }

    public List<Proxy> getUsedProxyList(){
        return usedProxyList;
    }

}
