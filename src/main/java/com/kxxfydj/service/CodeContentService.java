package com.kxxfydj.service;

import com.kxxfydj.entity.CodeContent;

import java.util.List;

/**
 * Created by kxxfydj on 2018/4/1.
 */
public interface CodeContentService {
    List<CodeContent> getFileChildren(CodeContent codeContent);
    CodeContent getFileFather(CodeContent codeContent);
    List<CodeContent> getFileAncestor(CodeContent codeContent);
    int saveOrUpdate(List<CodeContent> codeContentList);
    int saveOrUpdate(CodeContent codeContent);

    int addFile(CodeContent codeContent);
    int addFile(List<CodeContent> codeContentList);

    int updateFile(CodeContent codeContent);
    int updateFile(List<CodeContent> codeContentList);
}
