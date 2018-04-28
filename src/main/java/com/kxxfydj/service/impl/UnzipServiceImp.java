package com.kxxfydj.service.impl;

import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.service.CodeContentService;
import com.kxxfydj.service.UnzipService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kxxfydj on 2018/3/29.
 */
@Service
public class UnzipServiceImp implements UnzipService{
    private static final Logger logger = LoggerFactory.getLogger(UnzipServiceImp.class);

    @Autowired
    private CodeContentService codeContentService;

    @Override
    public void fileToDatabase(String filePath){
        List<CodeContent> codeContentList = new ArrayList<>();
        File fatherFile = new File(filePath);
        if(fatherFile.isFile() && !fatherFile.getAbsolutePath().endsWith(".zip")){
            handlerFileToDatabase(fatherFile,codeContentList,true);
        }else if(fatherFile.isDirectory()){
            handlerLeafFile(fatherFile,codeContentList);
        }
        codeContentService.saveOrUpdate(codeContentList);
    }

    private void handlerLeafFile(File file,List<CodeContent> codeContentList){
        if(file.isDirectory()) {
            File[] fileChildren = file.listFiles();
            for (File childFile : fileChildren) {
                if (childFile.isDirectory()) {
                    handlerLeafFile(childFile, codeContentList);
                } else {
                    handlerFileToDatabase(childFile, codeContentList, false);
                }
            }
        }
    }

    private void handlerFileToDatabase(File file,List<CodeContent> codeContentList,boolean isRoot){
        try(FileReader reader = new FileReader(file)){
            String fileString = IOUtils.toString(reader);
            CodeContent codeContent = new CodeContent();
            codeContent.setBody(fileString);
            codeContent.setEnabled(true);
            codeContent.setPath(file.getAbsolutePath());
            if(!isRoot){
                codeContent.setFatherPath(file.getParent());
            }
//            codeContentService.saveOrUpdate(codeContent);
            codeContentList.add(codeContent);
        }catch (IOException e){
            logger.error("文件到数据库时，文件读取出错！ 文件名：{}", file.getAbsolutePath(),e.getMessage(), e);
        }catch (Exception e){
            logger.error("数据保存到数据库出错！文件名：{}", file.getAbsolutePath(),e.getMessage(),e);
        }
    }
}
