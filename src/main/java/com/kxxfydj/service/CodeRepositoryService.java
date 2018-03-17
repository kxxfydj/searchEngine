package com.kxxfydj.service;

import com.kxxfydj.entity.CodeRepository;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/10.
 */
public interface CodeRepositoryService {
    List<CodeRepository> getRepositoryByName(String repositoryName);
    void addRepository(CodeRepository codeRepository);
    CodeRepository getRepositoryByNameAndLanguage(String repositoryName,String language);
    int refreshCount(CodeRepository codeRepository);
}
