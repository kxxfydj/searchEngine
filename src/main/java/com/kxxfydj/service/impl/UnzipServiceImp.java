package com.kxxfydj.service.impl;

import com.kxxfydj.common.FileSupportEnum;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.service.CodeContentService;
import com.kxxfydj.service.CodeInfoService;
import com.kxxfydj.service.UnzipService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Created by kxxfydj on 2018/3/29.
 */
@Service
public class UnzipServiceImp implements UnzipService {
    private static final Logger logger = LoggerFactory.getLogger(UnzipServiceImp.class);

    @Autowired
    private CodeContentService codeContentService;

    @Autowired
    private CodeInfoService codeInfoService;

//    private boolean truncate = false;

    @Override
    public void fileToDatabase(String repository, String filePath, boolean isUpdate) {
        ExecutorService sqlTransactionService = new ThreadPoolExecutor(4,8,5,TimeUnit.MINUTES,new LinkedBlockingDeque<>());
        File file = new File(filePath + File.separator + repository);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        if (file.isDirectory()) {
            File[] fileChildren = file.listFiles();
            for (File fileChild : fileChildren) {
                List<CodeContent> codeContentList = new ArrayList<>();
                String projectName = fileChild.getName();
                CodeInfo codeInfo = codeInfoService.getCodeInfoByProjectNameAndRepository(projectName, repository);
                if (codeInfo == null) {
                    logger.error("项目文件入库时发现没有对应的codeInfo项目对应该项目，该文件不入库！项目名：{} 仓库：{}", projectName,repository);
                    continue;
                }
                if (fileChild.isDirectory()) {
                    handlerLeafFile(codeInfo, fileChild, codeContentList);
                } else {
                    handlerFileToDatabase(codeInfo, fileChild, codeContentList, false);
                }
                dataToDatabase(sqlTransactionService, codeContentList, isUpdate);
            }

            if (!isUpdate) {
                try {
                    sqlTransactionService.shutdown();
                    sqlTransactionService.awaitTermination(10, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    logger.info("文件过大，文件数量过多,关闭当前数据库连接,不再存放数据");
                    sqlTransactionService.shutdownNow();
                }
            }
        }
        //将表清空标志恢复
//        this.truncate = false;
    }

    private void dataToDatabase(ExecutorService sqlTransactionService, List<CodeContent> codeContentList, boolean isUpdate) {
        logger.info("codeContentList size:{}", codeContentList.size());
        int handlerSize = 500;
        //如果没有清空过表，清空表
//        if(!truncate){
//            logger.info("开始清空codecontent表！");
//            codeContentService.truncateTable();
//            truncate = true;
//        }
        for (int i = 0; i < codeContentList.size(); i += handlerSize) {
            int end = i + handlerSize;
            int start = i;
            //文件更新的话，使用多线程，文件插入的话，使用单线程（mysql duplicate语句使用了select，会有锁，造成死锁）
            if (!isUpdate) {
                sqlTransactionService.execute(() -> codeContentService.addFile(codeContentList.subList(start, end > codeContentList.size() ? codeContentList.size() : end)));
            } else {
                codeContentService.saveOrUpdate(codeContentList.subList(start, end > codeContentList.size() ? codeContentList.size() : end));
            }
        }
    }

    private void handlerLeafFile(CodeInfo codeInfo, File file, List<CodeContent> codeContentList) {
        if (file.isDirectory() && !file.getName().contains(".git")) {
            File[] fileChildren = file.listFiles();
            for (File childFile : fileChildren) {
                if (childFile.isDirectory()) {
                    handlerLeafFile(codeInfo, childFile, codeContentList);
                } else {
                    handlerFileToDatabase(codeInfo, childFile, codeContentList, false);
                }
            }
        }
    }

    private void handlerFileToDatabase(CodeInfo codeInfo, File file, List<CodeContent> codeContentList, boolean isRoot) {
        try (FileReader reader = new FileReader(file)) {
            String filePath = file.getAbsolutePath();
            CodeContent codeContent = new CodeContent();

            FileSupportEnum language = FileSupportEnum.getLanguage(filePath);
            if(Objects.equals(language,FileSupportEnum.UnsupportFile)){
                return;
            }
            codeContent.setLanguage(language.getLanguage());

            //读取文件内容
            String body = IOUtils.toString(reader);
            codeContent.setBody(body);
            codeContent.setEnabled(true);
            codeContent.setPath(filePath);

            //判断是否为根目录，是根目录的话，没有parent信息
            if (!isRoot) {
                codeContent.setFatherPath(file.getParent());
            }

            codeContent.setCodeInfoId(codeInfo.getId());
            codeContent.setEnabled(true);
//            codeContentService.saveOrUpdate(codeContent);
            codeContentList.add(codeContent);
        } catch (IOException e) {
            logger.error("文件到数据库时，文件读取出错！ 文件名：{}", file.getAbsolutePath(), e.getMessage(), e);
        } catch (Exception e) {
            logger.error("数据保存到数据库出错！文件名：{}", file.getAbsolutePath(), e.getMessage(), e);
        }
    }
}
