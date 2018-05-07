package com.kxxfydj.mapper;

import com.kxxfydj.entity.CodeContent;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/31.
 */
public interface CodeContentMapper {
    List<CodeContent> selectAll();

    List<CodeContent> selectChildren(String path);
    CodeContent selectFather(String path);
    CodeContent selectFile(String filePath);
    List<CodeContent> selectAncestor(String path);
    int saveOrUpdate(List<CodeContent> codeContentList);
    int saveOrUpdateOne(CodeContent codeContent);

    int insert(CodeContent codeContent);
    int batchInsert(List<CodeContent> codeContentList);

    int update(CodeContent codeContent);
    int batchUpdate(List<CodeContent> codeContentList);
}
