package com.kxxfydj.dao;

import com.kxxfydj.entity.CodeInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/10.
 */
@Repository
public interface CodeInfoMapper {
    int batchInsert(List<CodeInfo> codeInfoListd);
    int delete(String projectName);
    List<CodeInfo> selectAll();
    List<CodeInfo> selectByProjectName(String projectName);
    List<CodeInfo> selectByProjectNameAndLanguage(String projectName,String language);
    CodeInfo selectByGitPath(String gitPath);
    int cleanTable();
}
