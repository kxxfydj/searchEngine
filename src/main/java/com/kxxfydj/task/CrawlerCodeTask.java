package com.kxxfydj.task;

import com.kxxfydj.crawler.Worker;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeRepository;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.service.CodeRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxfydj on 2018/3/25.
 */
@Component
public class CrawlerCodeTask {

    @Autowired
    private CrawlerConfig crawlerConfig;

    @Autowired
    private Worker worker;

    @Autowired
    CodeRepositoryService codeRepositoryService;

    @Value("#{settings['crawler.crawlerCode.hasCrawlered']}")
    private boolean hasCrawlered;

    @Scheduled(cron = "0 30 23/1 * * ?")
    public void crawlerCode() {
        hasCrawlered = true;
        List<CodeRepository> codeRepositoryList = codeRepositoryService.getInsertRepostitory();
        List<CrawlerTask> crawlerTaskList = generateCrawlerTasks(codeRepositoryList);
        worker.start(crawlerTaskList);
    }

//    @Scheduled(cron = "0 30 23 * * ?")
    public void updateCode() {
        if (!hasCrawlered) {
            crawlerCode();
            return;
        }
        List<CodeRepository> codeRepositoryList = codeRepositoryService.getUpdateRepostitory();
        List<CrawlerTask> crawlerTaskList = generateCrawlerTasks(codeRepositoryList);
        worker.start(crawlerTaskList);

    }

    private List<CrawlerTask> generateCrawlerTasks(List<CodeRepository> codeRepositoryList) {
        List<CrawlerTask> crawlerTaskList = new ArrayList<>();
        for (CodeRepository codeRepository : codeRepositoryList) {
            CrawlerTask crawlerTask = new CrawlerTask();
            crawlerTask.setCrawlerName(codeRepository.getCrawlerName());
            crawlerTask.setCodeFilePath(crawlerConfig.getCodePath());
            crawlerTask.setUrlCondition(codeRepository.getUrlCondition());
            crawlerTask.setRepository(codeRepository.getRepositoryName());
            crawlerTask.setFilterCount(codeRepository.getFilterCount());
            crawlerTask.setUpdate(codeRepository.isUpdate());
            crawlerTaskList.add(crawlerTask);
        }
        return crawlerTaskList;
    }


}
