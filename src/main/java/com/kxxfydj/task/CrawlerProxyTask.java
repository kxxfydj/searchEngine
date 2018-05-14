package com.kxxfydj.task;

import com.kxxfydj.crawler.Worker;
import com.kxxfydj.entity.CrawlerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxfydj on 2018/3/25.
 */
@Component
public class CrawlerProxyTask {

    @Autowired
    Worker worker;

    @Scheduled(cron = "0 0/30 * * * ? ")
    public void crawlerProxy(){
        List<CrawlerTask> crawlerTaskList = new ArrayList<>();
        CrawlerTask crawlerTask = new CrawlerTask();

        crawlerTask.setCrawlerName("xici");
        crawlerTaskList.add(crawlerTask);
        worker.start(crawlerTaskList);
    }
}
