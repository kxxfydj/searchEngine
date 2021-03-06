package com.kxxfydj.entity;

/**
 * Created by kxxfydj on 2018/3/10.
 */
public class CodeRepository {
    //id
    private int id;

    private String crawlerName;

    //the name of repository
    private String repositoryName;
    //the count of projects
    private long projectCount;
    // the condition of crawler
    private int filterCount;
    // the condition of url, conbine the url
    private String urlCondition;

    private boolean isUpdate;

    public String getCrawlerName() {
        return crawlerName;
    }

    public void setCrawlerName(String crawlerName) {
        this.crawlerName = crawlerName;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }



    public String getUrlCondition() {
        return urlCondition;
    }

    public void setUrlCondition(String urlCondition) {
        this.urlCondition = urlCondition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public long getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(long projectCount) {
        this.projectCount = projectCount;
    }

    public int getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }
}
