package com.kxxfydj.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
     *  是否开启返回响应头
     */
    private boolean isGetResponseHeaders = false;

    /**
     * 能接受的状态码
     */
    private Set<Integer> statusCodeSet;

    /**
     * 响应头
     */
    private Map<String, String> responseHeaders;

    private Object requestData;



    public static int getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }

    public Map<String, String> getHeaders() {
        if(headers == null){
            headers = new HashMap<>();
            headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
        }

        return headers;
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
        if(statusCodeSet == null){
            statusCodeSet = new HashSet<>();
            statusCodeSet.add(200);
            statusCodeSet.add(300);
            statusCodeSet.add(302);
        }
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
