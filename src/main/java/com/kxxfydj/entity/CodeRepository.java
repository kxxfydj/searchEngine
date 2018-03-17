package com.kxxfydj.entity;

/**
 * Created by kxxfydj on 2018/3/10.
 */
public class CodeRepository {
    //id
    private int id;
    //the name of repository
    private String repositoryName;
    //the count of projects
    private long projectCount;
    // the language of project
    private String language;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
