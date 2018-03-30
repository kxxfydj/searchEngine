package com.kxxfydj.entity;

/**
 * Created by kxxfydj on 2018/3/25.
 */
public class CrawlerTask {

    private String crawlerName;

    private String codeFilePath;

    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCrawlerName() {
        return crawlerName;
    }

    public void setCrawlerName(String crawlerName) {
        this.crawlerName = crawlerName;
    }

    public String getCodeFilePath() {
        return codeFilePath;
    }

    public void setCodeFilePath(String codeFilePath) {
        this.codeFilePath = codeFilePath;
    }
}
