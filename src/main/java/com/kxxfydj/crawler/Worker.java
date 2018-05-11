package com.kxxfydj.crawler;

import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.crawlerConfig.MyThreadPoolExecutor;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.ShutdownHookUtils;
import com.kxxfydj.utils.Threads;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by kxxfydj on 2016/7/28.
 */
@Service
public class Worker {

    private static final Logger logger = getLogger(Worker.class);

    /**
     * 配置文件
     */
    @Autowired
    private CrawlerConfig crawlerConfig;

    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    /**
     * 工作线程池
     */
    private MyThreadPoolExecutor workerExecutor;

    @PostConstruct
    public void init() {
        workerExecutor = new MyThreadPoolExecutor(crawlerConfig.getThreadPoolSizeCore(), crawlerConfig.getThreadPoolSizeMax(),
                0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(crawlerConfig.getThreadPoolSizeMax()));
        ShutdownHookUtils.hook(workerExecutor, crawlerConfig.getShutdownTimeoutSeconds());
    }

    /**
     * 爬虫框架启动入口
     */
    public void start(List<CrawlerTask> crawlerTask) {
        initTaskQueue(crawlerTask);
        while (true) {
            int activeCount = workerExecutor.getActiveCount();
            if (activeCount > 0) {
                logger.info("work_executor active count: {}", activeCount);
            }
            //活跃线程数不超过线程池最大值时才取任务
            if (activeCount < crawlerConfig.getThreadPoolSizeMax()) {
                try {
                    submitTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Threads.sleep(crawlerConfig.getThreadPoolIdleSleepTime(), TimeUnit.MILLISECONDS);
            }
            if (workerExecutor.isShutdown()) {
                break;
            }
        }
    }

    /**
     * 从任务队列中取出任务让执行线程池执行
     *
     * @throws InterruptedException
     */
    private void submitTask() throws InterruptedException {
        workerExecutor.submit(queue.take());
    }

    /**
     * 扫描指定路径下中的爬虫任务,初始化线程池的任务队列
     */
    private void initTaskQueue(List<CrawlerTask> crawlerTaskList) {
        try {
            for(CrawlerTask crawlerTask : crawlerTaskList) {
                //扫描指定包下的crawler任务,然后加入线程池任务队列
                Map<String, Class<Crawler>> supportCrawlerClazzMap = crawlerConfig.getSupportCrawlerClazzMap();
                Class<Crawler> clazz = supportCrawlerClazzMap.get(crawlerTask.getCrawlerName());
                if (clazz == null) {
                    logger.error("not found the crawler for the crawler task!");
                    continue;
                }
                Crawler crawler = ((Class<? extends Crawler>) clazz).newInstance();
                crawler.setCrawlerConfig(crawlerConfig);
                crawler.setCrawlerTask(crawlerTask);
                queue.add(crawler);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("class新建对象失败! message:{}", e.getMessage());
        }
    }


}