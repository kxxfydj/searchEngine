package com.kxxfydj.service.impl;

import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.mapper.CodeContentMapper;
import com.kxxfydj.service.CodeContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxfydj on 2018/4/1.
 */
@Service
public class CodeContentServiceImp implements CodeContentService {
    @Autowired
    CodeContentMapper codeContentMapper;

    @Override
    public List<CodeContent> getAllFiles() {
        return codeContentMapper.selectAll();
    }

    @Override
    public List<CodeContent> getFileChildren(String path) {
        return filterContentByDate(codeContentMapper.selectChildren(path));
    }

    @Override
    public CodeContent getFileFather(String path) {
        return codeContentMapper.selectFather(path);
    }

    @Override
    public CodeContent getFile(String path) {
        return codeContentMapper.selectFile(path);
    }

    @Override
    public List<CodeContent> getFileAncestor(String path) {
        return filterContentByDate(codeContentMapper.selectAncestor(path));
    }

    @Override
    public int addFile(CodeContent codeContent) {
        return codeContentMapper.insert(codeContent);
    }

    @Override
    public int addFile(List<CodeContent> codeContentList) {
        return codeContentMapper.batchInsert(codeContentList);
    }

    @Override
    public int updateFile(CodeContent codeContent) {
        return codeContentMapper.update(codeContent);
    }

    @Override
    public int updateFile(List<CodeContent> codeContentList) {
        return codeContentMapper.batchUpdate(codeContentList);
    }

    private List<CodeContent> filterContentByDate(List<CodeContent> codeContentList){
        List<CodeContent> filtedContentList = new ArrayList<>();
        codeContentList.stream()
                .filter(element ->
                        element.getAddTime().before(element.getUpdateTime()))
                .forEach(element ->
                        filtedContentList.add(element));
        if(filtedContentList.isEmpty()){
            return codeContentList;
        }else {
            return filtedContentList;
        }
    }

    @Override
    public int saveOrUpdate(List<CodeContent> codeContentList) {
        return codeContentMapper.saveOrUpdate(codeContentList);
    }

    @Override
    public int saveOrUpdate(CodeContent codeContent) {
        return codeContentMapper.saveOrUpdateOne(codeContent);
    }
}
