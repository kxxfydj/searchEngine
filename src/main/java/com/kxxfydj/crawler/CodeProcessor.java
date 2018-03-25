package com.kxxfydj.crawler;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.crawler.github.GitHubProcessor;
import com.kxxfydj.utils.CreateFileUtil;
import com.kxxfydj.utils.HeaderUtils;
import com.kxxfydj.utils.HttpsUtils;
import com.kxxfydj.utils.JsoupRequestData;
import com.sun.tools.javac.jvm.Code;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by kxxfydj on 2018/3/24.
 */
public abstract class CodeProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CodeProcessor.class);

    private Site site;

    private ExecutorService executorService;

    protected String host;

    protected String referer;

    protected String userAgent;

    public CodeProcessor(Site site) {
        this.site = site;
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void process(Page page) {
        String type = (String) page.getRequest().getExtra(CommonTag.TYPE);
        if(CommonTag.FIRST_PAGE.equals(type)){
            processFirstPage(page);
        }else if(CommonTag.NEXT_PAGE.equals(type)){
            processNextPage(page);
        }
    }

    private void processFirstPage(Page page){
        parseFirstPage(page);
        checkFinished(page);
    }

    private void processNextPage(Page page){
        Pair<String,String> stringTuple = parseNextPage(page);
        downloadZip(stringTuple.getValue0(),stringTuple.getValue1(),host,referer,userAgent);
        checkFinished(page);
    }

    abstract protected void parseFirstPage(Page page);

    private void downloadZip(String filePath, String downloadPath,String host,String referer,String userAgent){
        Map<String, String> requestHeaderMap;
        requestHeaderMap = HeaderUtils.initGetHeaders(host, referer, userAgent);
        JsoupRequestData jsoupRequestData = new JsoupRequestData();
        jsoupRequestData.setFiddlerProxy();
        jsoupRequestData.setMaxSize(100*1024*1024); //100MB
//        jsoupRequestData.setProxyFromSite(this.site);
        jsoupRequestData.setHeaders(requestHeaderMap);

        Future f = executorService.submit(new DownloadTask(filePath,downloadPath,jsoupRequestData));
        try{
            f.get(5, TimeUnit.MINUTES);
        }catch (Exception e){
            logger.error("文件下载超时，下载路径：{}", downloadPath);
        }
    }

    abstract protected Pair<String,String> parseNextPage(Page page);

    abstract protected void checkFinished(Page page);

    private class DownloadTask implements Runnable {

        private String downloadPath;

        private JsoupRequestData jsoupRequestData;

        private String filePath;

        DownloadTask(String filePath, String downloadPath, JsoupRequestData jsoupRequestData) {
            this.downloadPath = downloadPath;
            this.jsoupRequestData = jsoupRequestData;
            this.filePath = filePath;
        }

        @Override
        public void run() {
            logger.info("downloading file {}", downloadPath);
            byte[] binaryData = HttpsUtils.getBytes(downloadPath, jsoupRequestData, null);
            CreateFileUtil.generateFile(filePath, binaryData);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
