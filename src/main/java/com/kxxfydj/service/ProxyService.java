package com.kxxfydj.service;

import com.kxxfydj.entity.Proxy;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/15.
 */
public interface ProxyService {
    int saveProxiesOrUpdateEnabled(List<Proxy> proxyList);
    List<Proxy> getProxiesEnabled();
    int updateProxies(List<Proxy> proxyList);
    int updateProxy(Proxy proxy);
}
