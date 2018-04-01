package com.kxxfydj.task;

import com.kxxfydj.crawler.Worker;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeRepository;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.service.CodeContentService;
import com.kxxfydj.service.CodeRepositoryService;
import com.kxxfydj.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxfydj on 2018/3/25.
 */
@Component
public class CrawlerCodeTask {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerCodeTask.class);

    @Autowired
    private CrawlerConfig crawlerConfig;

    @Autowired
    private CodeContentService codeContentService;

    @Autowired
    private Worker worker;

    @Autowired
    CodeRepositoryService codeRepositoryService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void CrawlerCode(){

        List<CodeRepository> codeRepositoryList = codeRepositoryService.getAllRepostitory();

        for(CodeRepository codeRepository: codeRepositoryList) {
            CrawlerTask crawlerTask = new CrawlerTask();
            crawlerTask.setCrawlerName(codeRepository.getRepositoryName());
            crawlerTask.setCodeFilePath(crawlerConfig.getCodezipPath());
            worker.start(crawlerTask);
        }

        FileUtils.unzipFiles(crawlerConfig.getCodezipPath(),crawlerConfig);
        fileToDatabase(crawlerConfig.getCodeunzipPath());
    }

    public void fileToDatabase(String filePath){
        List<CodeContent> codeContentList = new ArrayList<>();
        File fatherFile = new File(filePath);
        if(fatherFile.isFile() && !fatherFile.getAbsolutePath().endsWith(".zip")){
            handlerFileToDatabase(fatherFile,codeContentList,true);
        }else if(fatherFile.isDirectory()){
            handlerLeafFile(fatherFile,codeContentList);
        }
//        codeContentService.saveOrUpdate(codeContentList);
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
            codeContentService.saveOrUpdate(codeContent);
            codeContentList.add(codeContent);
        }catch (IOException e){
            logger.error("文件到数据库时，文件读取出错！ 文件名：{}", file.getAbsolutePath(),e.getMessage(), e);
        }catch (Exception e){
            logger.error("数据保存到数据库出错！文件名：{}", file.getAbsolutePath(),e.getMessage(),e);
        }
    }
}
