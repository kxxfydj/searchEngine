package com.kxxfydj.entity;

/**
 * Created by kxxfydj on 2018/3/25.
 */
public class CrawlerTask {

    private String crawlerName;

    private String codeFilePath;

    private String urlCondition;

    private String repository;

    private boolean isUpdate;

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

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
