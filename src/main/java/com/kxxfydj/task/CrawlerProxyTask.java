package com.kxxfydj.task;

import com.kxxfydj.crawler.Worker;
import com.kxxfydj.entity.CrawlerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by kxxfydj on 2018/3/25.
 */
@Component
public class CrawlerProxyTask {

    @Autowired
    Worker worker;

    @Scheduled(cron = "0 0 /3 * * ?")
    public void crawlerProxy(){
        CrawlerTask crawlerTask = new CrawlerTask();
        crawlerTask.setCrawlerName("xici");
        worker.start(crawlerTask);
    }
}
