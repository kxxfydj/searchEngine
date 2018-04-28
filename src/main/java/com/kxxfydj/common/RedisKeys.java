package com.kxxfydj.common;

/**
 * Created by kxxfydj on 2018/4/25.
 */
public enum RedisKeys {

    DOCUMENTLIST("documentList"),
    CODEINFOID("codeInfoId"),
    PROXYLIST("proxyList");

    private String key;

    RedisKeys(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
