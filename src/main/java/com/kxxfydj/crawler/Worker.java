package com.kxxfydj.crawler;

import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.crawlerConfig.MyThreadPoolExecutor;
import com.kxxfydj.crawlerConfig.annotation.Crawl;
import com.kxxfydj.utils.ShutdownHookUtils;
import com.kxxfydj.utils.Threads;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * Created by chenwei on 2016/7/28.
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
    public void init(){
        workerExecutor = new MyThreadPoolExecutor(crawlerConfig.getThreadPoolSizeCore(), crawlerConfig.getThreadPoolSizeMax(),
                0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(crawlerConfig.getThreadPoolSizeMax()));
        ShutdownHookUtils.hook(workerExecutor, crawlerConfig.getShutdownTimeoutSeconds());
    }

    /**
     * 爬虫框架启动入口
     */
    public void start() {
        initTaskQueue();
        while (true) {
            int activeCount = workerExecutor.getActiveCount();
            if (activeCount > 0) {
                logger.info("work_executor active count: {}", activeCount);
            }
            //活跃线程数不超过线程池最大值时才取任务
            if (activeCount < crawlerConfig.getThreadPoolSizeMax()) {
                try {
                    submitTask();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            } else {
                Threads.sleep(crawlerConfig.getThreadPoolIdleSleepTime(), TimeUnit.MILLISECONDS);
            }
            if(workerExecutor.isShutdown()){
                break;
            }
        }
    }

    /**
     * 从任务队列中取出任务让执行线程池执行
     * @throws InterruptedException
     */
    private void submitTask() throws InterruptedException{
        workerExecutor.submit(queue.take());
    }

    /**
     * 扫描指定路径下中的爬虫任务,初始化线程池的任务队列
     */
    private void initTaskQueue(){
        try {
            //扫描指定包下的crawler任务,然后加入线程池任务队列
            List<Class<?>> clazzList = scanPackage(crawlerConfig.getSupportCrawlersPackages());
            for(Class<?> clazz : clazzList) {
                Constructor<?> constructor = clazz.getConstructor(String.class);
                Crawler crawler = (Crawler) constructor.newInstance("java");
//                Crawler crawler = ((Class<? extends Crawler>) clazz).newInstance();
                crawler.setCrawlerConfig(crawlerConfig);
                queue.add(crawler);
            }
//            | InvocationTargetException | NoSuchMethodException e
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("class新建对象失败! message:{}",e.getMessage());
        } catch (IOException e){
            logger.error("ClassPathResource读取文件夹失败! message:{}",e.getMessage());
        }
    }


    /**
     * 扫描爬虫包下注解了Crawl的爬虫任务类,然后加入任务队列
     * @param crawlPackage  扫描的包路径
     * @return  返回爬虫任务队列
     * @throws IOException
     */
    private List<Class<?>> scanPackage(String crawlPackage) throws IOException{
        List<Class<?>> clazzList = new ArrayList<>();
        if(StringUtils.isBlank(crawlPackage)){
            return clazzList;
        }

        //扫描任务包
        String path = crawlPackage.replaceAll("\\.","/" );
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
                    String classFileNamePrefix = classFileName.substring(0,classFileName.lastIndexOf(".class")).replaceAll("\\\\",".");
                    String classPath = crawlPackage + "." + fileName + "." + classFileNamePrefix;
                    Class<?> clazz = Class.forName(classPath);
                    Annotation[] annotations = clazz.getAnnotations();
                    //只有被crawl注解的类才被加入任务队列中
                    for(Annotation annotation : annotations){
                        if(annotation != null && annotation instanceof Crawl){
                            clazzList.add(clazz);
                            break;
                        }
                    }
                }catch (ClassNotFoundException e){
                    logger.error("读取class文件出错!   message:{}",e.getMessage());
                }
            });
        });

        return clazzList;
    }

}