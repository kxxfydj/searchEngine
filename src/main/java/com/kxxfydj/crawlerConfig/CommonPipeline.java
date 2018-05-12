package com.kxxfydj.crawlerConfig;

import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.enginer.EngineConfig;
import com.kxxfydj.enginer.IndexManager;
import com.kxxfydj.entity.*;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.*;
import com.kxxfydj.utils.ApplicationContextUtils;
import com.kxxfydj.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class CommonPipeline implements Pipeline {

    private static Logger logger = LoggerFactory.getLogger(CommonPipeline.class);

    private CodeInfoService codeInfoService;

    private CodeRepositoryService codeRepositoryService;

    private ProxyService proxyService;

    private RedisUtil redisUtil;

    private UnzipService unzipService;

    private CrawlerConfig crawlerConfig;

    private CrawlerTask crawlerTask;

    private CodeContentService codeContentService;

    private EngineConfig engineConfig;

    public CommonPipeline(CrawlerTask crawlerTask, CrawlerConfig crawlerConfig) {
        codeInfoService = ApplicationContextUtils.getBean(CodeInfoService.class);
        codeRepositoryService = ApplicationContextUtils.getBean(CodeRepositoryService.class);
        proxyService = ApplicationContextUtils.getBean(ProxyService.class);
        redisUtil = ApplicationContextUtils.getBean(RedisUtil.class);
        unzipService = ApplicationContextUtils.getBean(UnzipService.class);
        this.codeContentService = ApplicationContextUtils.getBean(CodeContentService.class);
        this.engineConfig = ApplicationContextUtils.getBean(EngineConfig.class);
        this.crawlerConfig = crawlerConfig;
        this.crawlerTask = crawlerTask;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        boolean isFinal = false;
        if (resultItems.get(PipelineKeys.FINISHED) != null) {
            isFinal = resultItems.get(PipelineKeys.FINISHED);
        }

        if (isFinal) {
            Object proxyListObject = resultItems.get(PipelineKeys.PROXY_LIST);
            if (proxyListObject != null && proxyListObject instanceof List) {
                processProxy((List<Proxy>) proxyListObject);
            }

            Object object = resultItems.get(PipelineKeys.CODEINFO_LIST);
            if (object != null && object instanceof List) {
                if (!crawlerTask.isUpdate()) {
                    processInsertCodeInfo((List<CodeInfo>) object, resultItems);
                }
            }

            Object codeContentListObj = resultItems.get(PipelineKeys.CODECONTENT_LIST);
            if (codeContentListObj != null && codeContentListObj instanceof List) {
                if (crawlerTask.isUpdate()) {
                    processUpdateCodeContent((List<CodeContent>) codeContentListObj, resultItems);
                }
            }
        }
    }

    /**
     * 持久化代理数据到数据库
     */
    private void processProxy(List<Proxy> proxyList) {
        int rows;
        long startTime = System.currentTimeMillis();

        rows = proxyService.saveProxiesOrUpdateEnabled(proxyList);

        long endTime = System.currentTimeMillis();
        logger.info("更新和插入了{}条数据", rows);
        logger.info("共耗时{}毫秒", endTime - startTime);

        redisUtil.lSet(RedisKeys.PROXYLIST.getKey(), proxyList);
        logger.info("更新redis代理缓存{}条数据", proxyList.size());
    }

    /**
     * 持久化更新代码仓库数据到数据库
     */
    private void processUpdateCodeContent(List<CodeContent> codeContentList, ResultItems resultItems) {
        logger.info("{}爬取任务完成，开始信息持久化和缓存操作！",crawlerTask.getCrawlerName());
        long startTime = System.currentTimeMillis();
        int rows = codeContentService.saveOrUpdate(codeContentList);
        long endTime = System.currentTimeMillis();
        logger.info("数据库codeContent表更新完成，共修改{}条数据，共耗时：{}毫秒",rows,endTime - startTime);

        logger.info("开始文件系统文件更新");
        for(CodeContent codeContent: codeContentList){
            FileUtils.updateFiles(codeContent.getPath(),codeContent.getBody());
        }
    }

    /**
     * 持久化插入代码仓库数据到数据库
     */
    private void processInsertCodeInfo(List<CodeInfo> codeInfoList, ResultItems resultItems) {
        logger.info("{}爬取任务结束，开始信息持久化和缓存操作！", crawlerTask.getCrawlerName());
        String cralwerType = resultItems.get(PipelineKeys.CRAWLER_TYPE);

        //初始化house的cityId
        int rows;
        long startTime = System.currentTimeMillis();

        CodeRepository codeRepository = codeRepositoryService.getRepositoryByName(cralwerType);
        int codeRepositoryId = codeRepository.getId();
        int count = 0;
        for (CodeInfo codeInfo : codeInfoList) {
            codeInfo.setRepositoryId(codeRepositoryId);
            count++;
        }

        //更新codeRepository表
        codeRepository.setProjectCount(count);
        rows = codeRepositoryService.refreshCount(codeRepository);
        logger.info("更新coderepository表{}条数据", rows);

        //更新codeInfo表
        rows = codeInfoService.saveOrUpdate(codeInfoList);
        long endTime = System.currentTimeMillis();
        logger.info("codeInfo表数据更新完成! 共插入{}条数据!共耗时:{}毫秒!", rows, endTime - startTime);

        //从数据库中重新读取codeInfo信息，更新codeinfo对象中的id信息
        codeInfoList = codeInfoService.getAllCodeInfo();

        //更新redis缓存
        List<String> codeInfoKeys = new ArrayList<>();
        for (CodeInfo codeInfo : codeInfoList) {
            codeInfoKeys.add(RedisKeys.CODEINFOID.getKey() + ":" + codeInfo.getId());
        }
        redisUtil.setForBatch(codeInfoKeys, codeInfoList);
        logger.info("codeInfo对象缓存到redis中！共{}条", codeInfoKeys.size());

        //解压文件，并将文件保存到数据库codeContent表
        logger.info("开始解压文件！");
        FileUtils.unzipFiles(crawlerConfig.getCodezipPath() + File.separator + crawlerTask.getRepository(),
                crawlerConfig.getCodeunzipPath() + File.separator + crawlerTask.getRepository());
        logger.info("文件入库！");
        unzipService.fileToDatabase(crawlerTask.getRepository(), crawlerConfig.getCodeunzipPath(), false);

        //开始生成搜索全文索引
        logger.info("开始生成搜索全文索引");
        startTime = System.currentTimeMillis();
        List<CodeContent> codeContentList = codeContentService.getAllFiles();
        IndexManager indexManager = new IndexManager(engineConfig);
        indexManager.createIndex(codeContentList, redisUtil);
        endTime = System.currentTimeMillis();
        logger.info("搜索全文索引生成完成，共耗时：{}毫秒", endTime - startTime);
        logger.info("{}任务完成！", crawlerTask.getCrawlerName());
    }
}
