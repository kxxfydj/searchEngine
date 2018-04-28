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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private Worker worker;

    @Autowired
    CodeRepositoryService codeRepositoryService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void CrawlerCode(){

        List<CodeRepository> codeRepositoryList = codeRepositoryService.getAllRepostitory();
        List<CrawlerTask> crawlerTaskList = new ArrayList<>();

        for(CodeRepository codeRepository: codeRepositoryList) {
            CrawlerTask crawlerTask = new CrawlerTask();
            crawlerTask.setCrawlerName(codeRepository.getRepositoryName());
            crawlerTask.setCodeFilePath(crawlerConfig.getCodezipPath());
            crawlerTask.setUrlCondition(codeRepository.getUrlCondition());
            crawlerTask.setFilterCount(codeRepository.getFilterCount());
            crawlerTaskList.add(crawlerTask);
        }
        worker.start(crawlerTaskList);

    }


}
