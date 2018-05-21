package com.kxxfydj.crawlerConfig;

import com.kxxfydj.common.SystemConstants;
import com.kxxfydj.crawler.Crawler;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by chenwei on 2016/7/29.
 */
@Component
public class CrawlerConfig {
    private static final Logger logger = getLogger(CrawlerConfig.class);

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

    @Value("#{settings['crawler.codefile.path']}")
    private String codePath;

    private String codeCommitFilePath;

    @Value("#{settings['crawler.proxy.switch']}")
    private boolean proxySwitch;

    private Map<String ,Class<Crawler>> supportCrawlerClazzMap = new HashMap<>();

    public Map<String, Class<Crawler>> getSupportCrawlerClazzMap() {
        return supportCrawlerClazzMap;
    }

    @PostConstruct
    public void init(){
        loadSupportCrawlerClazzMap(getSupportCrawlersPackages());
        logger.info("supportCrawlerClazzMap size:{}",supportCrawlerClazzMap.size());
        if(debugSwitch != null && debugSwitch == true){
            System.setProperty(SystemConstants.GLOBAL_DEBUG_SWITCH, "true");
        }
    }

    /**
     * 扫描爬虫包下注解了Crawl的爬虫任务类,然后加入任务队列
     * @param crawlPackage  扫描的包路径
     * @return  返回爬虫任务队列
     * @throws IOException
     */
    private void loadSupportCrawlerClazzMap(String crawlPackage){
        if(StringUtils.isBlank(crawlPackage)){
            logger.warn("爬虫扫描包参数为空");
            return;
        }

        try {
            //扫描任务包
            String path = crawlPackage.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
            Resource resource = new ClassPathResource(path);
            File resourceFile = resource.getFile();
            //去除不是包的文件
            File[] crawlerPackage = resourceFile.listFiles(File::isDirectory);

            Arrays.stream(crawlerPackage).forEach(file -> {
                String fileName = file.getName();
                File[] classFiles = file.listFiles();
                assert classFiles != null;
                Arrays.stream(classFiles).forEach(classFile -> {
                    try {
                        //对任务包下的所有类文件加载
                        String classFileName = classFile.getName();
                        String classFileNamePrefix = classFileName.substring(0, classFileName.lastIndexOf(".class")).replaceAll(Pattern.quote(File.separator), ".");
                        String classPath = crawlPackage + "." + fileName + "." + classFileNamePrefix;
                        Class<?> clazz = Class.forName(classPath);
                        Annotation[] annotations = clazz.getAnnotations();
                        //只有被crawl注解的类才被加入任务队列中
                        for (Annotation annotation : annotations) {
                            if (annotation != null && annotation instanceof Crawl) {
                                addSupportCrawlerToMap((Class<Crawler>)clazz,(Crawl) annotation);
                                break;
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        logger.error("读取class文件出错!   message:{}", e.getMessage(),e);
                    }
                });
            });
        }catch (IOException e){
            logger.info("读取爬虫类包出错！",e.getMessage(),e);
        }
    }

    private void addSupportCrawlerToMap(Class<Crawler> clazz, Crawl crawl){
        String crawlerName = crawl.crawlerName();
        if(supportCrawlerClazzMap.containsKey(crawlerName)){
            Class<Crawler> oldClazz = supportCrawlerClazzMap.get(crawlerName);
            logger.error("爬虫文件任务出现重名！请确认：{},{}",oldClazz.getName(),clazz.getName());
        }
        supportCrawlerClazzMap.put(crawlerName,clazz);
    }

    public boolean isProxySwitch() {
        return proxySwitch;
    }

    public void setProxySwitch(boolean proxySwitch) {
        this.proxySwitch = proxySwitch;
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

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }
}
