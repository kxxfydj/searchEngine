package com.kxxfydj.service.impl;

import com.kxxfydj.mapper.CodeInfoMapper;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.service.CodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/10.
 */
@Service
public class CodeInfoServiceImp implements CodeInfoService{
    @Autowired
    private CodeInfoMapper codeInfoMapper;

    @Override
    public int saveCodeInfo(List<CodeInfo> codeInfoList) {
        return codeInfoMapper.batchInsert(codeInfoList);
    }

    @Override
    public List<CodeInfo> getCodeInfoByProjectName(String projectName) {
        return codeInfoMapper.selectByProjectName(projectName);
    }

    @Override
    public List<CodeInfo> getCodeInfoByProjectNameAndLanguage(String projectName, String language) {
        return codeInfoMapper.selectByProjectNameAndLanguage(projectName,language);
    }

    @Override
    public CodeInfo getCodeInfoByGitPath(String gitPath) {
        return codeInfoMapper.selectByGitPath(gitPath);
    }

    @Override
    public int cleanCodeInfo() {
        return codeInfoMapper.truncateTable();
    }
}
