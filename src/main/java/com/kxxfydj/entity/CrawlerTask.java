package com.kxxfydj.entity;

/**
 * Created by kxxfydj on 2018/3/25.
 */
public class CrawlerTask {

    private String crawlerName;

    private String codeFilePath;

    private String urlCondition;

    private int filterCount = Integer.MAX_VALUE;

    public int getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }

    public String getUrlCondition() {
        return urlCondition;
    }

    public void setUrlCondition(String urlCondition) {
        this.urlCondition = urlCondition;
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
