package com.kxxfydj.crawlerConfig;

import com.kxxfydj.common.SystemConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by chenwei on 2016/7/29.
 */
@Component
public class CrawlerConfig {
    private static final Logger log = getLogger(CrawlerConfig.class);

    @Value("#{settings['crawler.support.crawlers.packages']}")
    private String supportCrawlersPackages;

    @Value("#{settings['crawler.thread.pool.size.core']}")
    private int threadPoolSizeCore;

    @Value("#{settings['crawler.thread.pool.size.max']}")
    private int threadPoolSizeMax;

    @Value("#{settings['crawler.global.retry.times']}")
    private int globalRetryTimes;

    @Value("#{settings['crawler.thread.pool.idle.sleepTime']}")
    private int threadPoolIdleSleepTime;

    @Value("#{settings['crawler.global.sleepTime']}")
    private int globalSleepTime;

    @Value("#{settings['crawler.global.spider.thread.size']}")
    private int spiderThreadSize;

    @Value("#{settings['crawler.global.userAgent']}")
    private String globalUserAgent;

    @Value("#{settings['crawler.shutdown.timeout.seconds']}")
    private int shutdownTimeoutSeconds;

    @Value("#{settings['crawler.file.path']}")
    private String filePath;

    @Value("#{settings['global.debug.switch']}")
    private Boolean debugSwitch;

    @PostConstruct
    public void init(){
        if(debugSwitch != null && debugSwitch == true){
            System.setProperty(SystemConstants.GLOBAL_DEBUG_SWITCH, "true");
        }
    }

    public int getThreadPoolSizeCore() {
        return threadPoolSizeCore;
    }

    public void setThreadPoolSizeCore(int threadPoolSizeCore) {
        this.threadPoolSizeCore = threadPoolSizeCore;
    }

    public int getThreadPoolSizeMax() {
        return threadPoolSizeMax;
    }

    public void setThreadPoolSizeMax(int threadPoolSizeMax) {
        this.threadPoolSizeMax = threadPoolSizeMax;
    }

    public int getGlobalRetryTimes() {
        return globalRetryTimes;
    }

    public void setGlobalRetryTimes(int globalRetryTimes) {
        this.globalRetryTimes = globalRetryTimes;
    }

    public int getThreadPoolIdleSleepTime() {
        return threadPoolIdleSleepTime;
    }

    public void setThreadPoolIdleSleepTime(int threadPoolIdleSleepTime) {
        this.threadPoolIdleSleepTime = threadPoolIdleSleepTime;
    }

    public int getGlobalSleepTime() {
        return globalSleepTime;
    }

    public void setGlobalSleepTime(int globalSleepTime) {
        this.globalSleepTime = globalSleepTime;
    }

    public int getSpiderThreadSize() {
        return spiderThreadSize;
    }

    public void setSpiderThreadSize(int spiderThreadSize) {
        this.spiderThreadSize = spiderThreadSize;
    }

    public String getGlobalUserAgent() {
        return globalUserAgent;
    }

    public void setGlobalUserAgent(String globalUserAgent) {
        this.globalUserAgent = globalUserAgent;
    }

    public int getShutdownTimeoutSeconds() {
        return shutdownTimeoutSeconds;
    }

    public void setShutdownTimeoutSeconds(int shutdownTimeoutSeconds) {
        this.shutdownTimeoutSeconds = shutdownTimeoutSeconds;
    }

    public String getSupportCrawlersPackages() {
        return supportCrawlersPackages;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getDebugSwitch() {
        return debugSwitch;
    }

    public void setDebugSwitch(Boolean debugSwitch) {
        this.debugSwitch = debugSwitch;
    }
}
