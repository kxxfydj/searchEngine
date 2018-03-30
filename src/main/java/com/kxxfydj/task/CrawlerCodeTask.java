package com.kxxfydj.task;

import com.kxxfydj.crawler.Worker;
import com.kxxfydj.entity.CodeRepository;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.service.CodeRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/25.
 */
@Component
public class CrawlerCodeTask {

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
            worker.start(crawlerTask);
        }
    }
}
