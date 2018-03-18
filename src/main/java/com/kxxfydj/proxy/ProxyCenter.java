package com.kxxfydj.proxy;

import com.kxxfydj.entity.Proxy;
import com.kxxfydj.mapper.ProxyMapper;
import com.kxxfydj.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Lazy(false)
public class ProxyCenter {

    private static Map<String, java.net.Proxy.Type> typeMap = new HashMap<>();

    @Autowired
    private ProxyService proxyService;

    static {
        typeMap.put("http", java.net.Proxy.Type.HTTP);
        typeMap.put("https", java.net.Proxy.Type.HTTP);
        typeMap.put("socks4/5", java.net.Proxy.Type.SOCKS);
    }

    public synchronized java.net.Proxy availableProxy(List<Proxy> autoProxyList) {
        int j = 0;
        for(int i = 1; i < autoProxyList.size(); i++){
            if(autoProxyList.get(i).getSpeed() < autoProxyList.get(j).getSpeed() && autoProxyList.get(j).isEnabled() && autoProxyList.get(i).isEnabled()){
                j = i;
            }
        }
        Proxy autoProxy = autoProxyList.get(j);
        InetSocketAddress socketAddress = new InetSocketAddress(autoProxy.getIp(), autoProxy.getPort());

        autoProxy.setUsedTimes(autoProxy.getUsedTimes() + 1);
        proxyService.updateProxy(autoProxy);

        return new java.net.Proxy(typeMap.get(autoProxy.getType()),socketAddress);
    }

}
