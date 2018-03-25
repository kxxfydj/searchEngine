package com.kxxfydj.crawlerConfig;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CodeRepository;
import com.kxxfydj.entity.Proxy;
import com.kxxfydj.service.CodeInfoService;
import com.kxxfydj.service.CodeRepositoryService;
import com.kxxfydj.service.ProxyService;
import com.kxxfydj.utils.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class CommonPipeline implements Pipeline {

    private static Logger logger = LoggerFactory.getLogger(CommonPipeline.class);

    private CodeInfoService codeInfoService;

    private CodeRepositoryService codeRepositoryService;

    private ProxyService proxyService;

    public CommonPipeline() {
        codeInfoService = ApplicationContextUtils.getBean(CodeInfoService.class);
        codeRepositoryService = ApplicationContextUtils.getBean(CodeRepositoryService.class);
        proxyService = ApplicationContextUtils.getBean(ProxyService.class);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        boolean isFinal = false;
        if (resultItems.get(PipelineKeys.FINISHED) != null) {
            isFinal = resultItems.get(PipelineKeys.FINISHED);
        }

        if (isFinal) {
            Object proxyListObject = resultItems.get(PipelineKeys.PROXY_LIST);
            if(proxyListObject != null && proxyListObject instanceof List){
                processProxy((List<Proxy>) proxyListObject);
            }


            Object object = resultItems.get(PipelineKeys.CODEINFO_LIST);
            if (object != null && object instanceof List) {
                processCodeInfo((List<CodeInfo>) object,resultItems);
            }
        }
    }

    /**
     * 持久化代理数据到数据库
     * @param proxyList
     */
    private void processProxy(List<Proxy> proxyList){
        int rows;
        long startTime = System.currentTimeMillis();

        rows = proxyService.saveProxiesOrUpdateEnabled(proxyList);

        long endTime = System.currentTimeMillis();
        logger.info("更新和插入了{}条数据",rows);
        logger.info("共耗时{}毫秒",endTime - startTime);
    }

    /**
     * 持久化代码仓库数据到数据库
     * @param codeInfoList
     * @param resultItems
     */
    private void processCodeInfo(List<CodeInfo> codeInfoList,ResultItems resultItems){
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

        codeRepository.setProjectCount(count);

        rows = codeRepositoryService.refreshCount(codeRepository);
        logger.info("更新coderepository表{}条数据", rows);

        rows = codeInfoService.cleanCodeInfo();
        logger.info("删除codeInfo表原有数据{}条!", rows);

        rows = codeInfoService.saveCodeInfo(codeInfoList);
        long endTime = System.currentTimeMillis();
        logger.info("数据插入完成! 共插入{}条数据!", rows);
        logger.info("共耗时:{}毫秒!", endTime - startTime);
    }
}
