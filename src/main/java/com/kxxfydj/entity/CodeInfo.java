package com.kxxfydj.entity;

/**
 * Created by kxxfydj on 2018/3/9.
 */
public class CodeInfo {

    //id
    private int id;
    //the code repository id
    private int repositoryId;
    //项目名称
    private String projectName;
    //代码来源仓库
    private String repository;
    //语言
    private String language;
    //项目描述
    private String description;
    //本地保存文件路径
    private String filePath;
    //stars number
    private int stars;
    //git repository path
    private String gitPath;

    public int getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getGitPath() {
        return gitPath;
    }

    public void setGitPath(String gitPath) {
        this.gitPath = gitPath;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
