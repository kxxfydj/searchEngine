package com.kxxfydj.service.impl;

import com.kxxfydj.entity.Proxy;
import com.kxxfydj.mapper.ProxyMapper;
import com.kxxfydj.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/15.
 */
@Service
public class ProxyServiceImp implements ProxyService {
    @Autowired
    ProxyMapper proxyMapper;

    @Override
    public int saveProxiesOrUpdateEnabled(List<Proxy> proxyList) {
        return proxyMapper.saveOrUpdate(proxyList);
    }

    @Override
    public List<Proxy> getProxiesEnabled() {
        return proxyMapper.selectEnabled();
    }

    @Override
    public int updateProxies(List<Proxy> proxyList) {
        return proxyMapper.batchUpdate(proxyList);
    }

    @Override
    public int updateProxy(Proxy proxy) {
        return proxyMapper.update(proxy);
    }
}
