package com.kxxfydj.mapper;

import com.kxxfydj.entity.CodeInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/10.
 */
@Repository
public interface CodeInfoMapper {
    int batchInsert(List<CodeInfo> codeInfoListd);
    int truncateTable();
    int deleteByProjectName(String projectName);
    List<CodeInfo> selectAll();
    List<CodeInfo> selectByProjectName(String projectName);
    List<CodeInfo> selectByProjectNameAndLanguage(@Param("projectName") String projectName, @Param("language") String language);
    CodeInfo selectByGitPath(String gitPath);
    CodeInfo selectByProjectNameAndRepository(@Param("projectName") String projectName,@Param("repository") String repository);
    int insertOrUpdate(List<CodeInfo> codeInfoList);
}
