package com.kxxfydj.service;

import com.kxxfydj.entity.CodeContent;

import java.util.List;

/**
 * Created by kxxfydj on 2018/4/1.
 */
public interface CodeContentService {

    List<CodeContent> getAllFiles();
    List<CodeContent> getFileChildren(String path);
    CodeContent getFileFather(String path);
    CodeContent getFile(String path);
    List<CodeContent> getFileAncestor(String path);
    List<String> getFileByCodeInfoId(int codeInfoId);
    int saveOrUpdate(List<CodeContent> codeContentList);
    int saveOrUpdate(CodeContent codeContent);

    int addFile(CodeContent codeContent);
    int addFile(List<CodeContent> codeContentList);

    int updateFile(CodeContent codeContent);
    int updateFile(List<CodeContent> codeContentList);
}
