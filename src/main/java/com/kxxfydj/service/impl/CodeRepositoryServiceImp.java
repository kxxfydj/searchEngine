package com.kxxfydj.service.impl;

import com.kxxfydj.entity.CodeRepository;
import com.kxxfydj.mapper.CodeRepositoryMapper;
import com.kxxfydj.service.CodeRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/10.
 */
@Service
public class CodeRepositoryServiceImp implements CodeRepositoryService {
    @Autowired
    CodeRepositoryMapper codeRepositoryMapper;

    @Override
    public List<CodeRepository> getRepositoryByName(String repositoryName) {
        return codeRepositoryMapper.selectByName(repositoryName);
    }

    @Override
    public void addRepository(CodeRepository codeRepository) {
        codeRepositoryMapper.insert(codeRepository);
    }

    @Override
    public CodeRepository getRepositoryByNameAndLanguage(String repositoryName, String language) {
        return codeRepositoryMapper.selectByNameAndLanguage(repositoryName,language);
    }

    @Override
    public int refreshCount(CodeRepository codeRepository) {
        return codeRepositoryMapper.updateCodeRepository(codeRepository);
    }
}
