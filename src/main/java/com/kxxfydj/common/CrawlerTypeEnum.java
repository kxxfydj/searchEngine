package com.kxxfydj.common;

/**
 * Created by kxxfydj on 2018/3/13.
 */
public enum CrawlerTypeEnum {
    GITLAB("gitlab"),
    GITHUB("github");

    private String type;

    CrawlerTypeEnum(String value){
        this.type = value;
    }

    public String getType(){
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
