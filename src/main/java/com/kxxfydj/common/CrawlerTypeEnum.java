package com.kxxfydj.common;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/3/13.
 */
public enum RepositoryEnum {
    GITLAB("gitlab"),
    GITHUB("github");

    private String repository;

    private static final Map<String,String> repositoryMap = new HashMap<>();

    static {
        repositoryMap.put("github",GITHUB.getRepository());
        repositoryMap.put("gitlab",GITLAB.getRepository());
    }

    RepositoryEnum(String repository){
        this.repository = repository;
    }

    private String getRepository(){
        return this.repository;
    }

    public String getRepository(String crawlerType){
        return repositoryMap.get(crawlerType);
    }
}
