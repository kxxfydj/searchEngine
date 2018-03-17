package com.kxxfydj.mapper;

import com.kxxfydj.entity.Proxy;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/15.
 */
public interface ProxyMapper {
    int saveOrUpdate(List<Proxy> proxyList);
    List<Proxy> selectEnabled();
    int batchUpdate(List<Proxy> proxyList);
    int update(Proxy proxy);
}
