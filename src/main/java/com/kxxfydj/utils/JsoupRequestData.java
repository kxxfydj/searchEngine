package com.kxxfydj.utils;


import com.kxxfydj.entity.Proxy;
import com.kxxfydj.proxy.ProxyCenter;
import com.kxxfydj.redis.RedisUtil;
import org.apache.http.HttpStatus;
import org.springframework.web.context.ContextLoader;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by kxxfydj on 2018/3/5.
 */
public class JsoupRequestData {
    /**
     * 默认超时毫秒数
     */
    private static final int DEFAULT_TIMEOUT = 15000;
    /**
     * 请求头信息
     */
    private Map<String, String> headers;

    /**
     * 是否保留请求头
     */
    private boolean keepHeaders = true;

    /**
     * 是否支持自动跳转
     */
    private Boolean isAutoRedirect = true;

    /**
     * 是否开启返回响应头
     */
    private boolean isGetResponseHeaders = false;

    /**
     * 代理设置
     */
    private java.net.Proxy proxy;

    /**
     * 能接受的状态码
     */
    private Set<Integer> statusCodeSet;

    /**
     * 响应头
     */
    private Map<String, String> responseHeaders;

    private Object requestData;


    public java.net.Proxy getProxy() {
        return proxy;
    }

    public void setProxy(java.net.Proxy proxy) {
        this.proxy = proxy;
    }

    public static int getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }

    public JsoupRequestData(){
        initJsoupRequestData();
    }

    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
        }

        return headers;
    }

    private void initJsoupRequestData() {
//        if(this.proxy == null){
////            InetSocketAddress defaultSocket = new InetSocketAddress("127.0.0.1",)
////            java.net.Proxy defaultProxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,)
////            this.proxy =
//        }

        statusCodeSet = new HashSet<>();
        statusCodeSet.add(HttpStatus.SC_OK);
        statusCodeSet.add(HttpStatus.SC_MOVED_PERMANENTLY);
        statusCodeSet.add(HttpStatus.SC_MOVED_TEMPORARILY);
        statusCodeSet.add(HttpStatus.SC_SEE_OTHER);
        statusCodeSet.add(HttpStatus.SC_TEMPORARY_REDIRECT);
    }

    public void setProxyFromRedis() {
        if (ContextLoader.getCurrentWebApplicationContext() != null) {
            ProxyCenter proxyCenter = ContextLoader.getCurrentWebApplicationContext().getBean(ProxyCenter.class);
            RedisUtil<String, Proxy> redisTemplate = ContextLoader.getCurrentWebApplicationContext().getBean(RedisUtil.class);
            List<Proxy> proxyList = redisTemplate.lGet("proxyList", 0, 99);
            this.proxy = proxyCenter.availableProxy(proxyList);
        }
    }

    public void setFiddlerProxy() {
        InetSocketAddress fiddlerSocket = new InetSocketAddress("127.0.0.1",888 );
        this.proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, fiddlerSocket);
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean isKeepHeaders() {
        return keepHeaders;
    }

    public void setKeepHeaders(boolean keepHeaders) {
        this.keepHeaders = keepHeaders;
    }

    public Boolean getAutoRedirect() {
        return isAutoRedirect;
    }

    public void setAutoRedirect(Boolean autoRedirect) {
        isAutoRedirect = autoRedirect;
    }

    public boolean isGetResponseHeaders() {
        return isGetResponseHeaders;
    }

    public void setGetResponseHeaders(boolean getResponseHeaders) {
        isGetResponseHeaders = getResponseHeaders;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Set<Integer> getStatusCodeSet() {
        return statusCodeSet;
    }

    public void setStatusCodeSet(Set<Integer> statusCodeSet) {
        this.statusCodeSet = statusCodeSet;
    }

    public Object getRequestData() {
        return requestData;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }
}
