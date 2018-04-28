package com.kxxfydj.service;

import com.kxxfydj.entity.CodeInfo;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/10.
 */
public interface CodeInfoService {
    int saveCodeInfo(List<CodeInfo> codeInfoList);
    List<CodeInfo> getCodeInfoByProjectName(String projectName);
    List<CodeInfo> getCodeInfoByProjectNameAndLanguage(String projectName,String language);
    List<CodeInfo> getAllCodeInfo();
    CodeInfo getCodeInfoByGitPath(String gitPath);
    int cleanCodeInfo();
}
