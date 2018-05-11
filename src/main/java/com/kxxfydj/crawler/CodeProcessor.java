package com.kxxfydj.crawler;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.CreateFileUtil;
import com.kxxfydj.utils.HeaderUtils;
import com.kxxfydj.utils.HttpsUtils;
import com.kxxfydj.utils.JsoupRequestData;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by kxxfydj on 2018/3/24.
 */
public abstract class CodeProcessor implements PageProcessor {

    protected static final Logger logger = LoggerFactory.getLogger(CodeProcessor.class);

    protected CrawlerTask crawlerTask;

    protected String filePath;

    private Site site;

    private ExecutorService executorService;

    protected String host;

    protected String referer;

    protected String userAgent;

    public CodeProcessor(Site site, CrawlerTask crawlerTask) {
        this.crawlerTask = crawlerTask;
        this.site = site;
        this.filePath = crawlerTask.getCodeFilePath() + File.separator + crawlerTask.getCrawlerName();
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
        Triplet<String,String, CodeInfo> stringTuple = parseNextPage(page);
        boolean isSuccess = downloadZip(stringTuple.getValue0(),stringTuple.getValue1(),host,referer,userAgent);
        afterDownload(isSuccess,stringTuple.getValue2());
        checkFinished(page);
    }

    abstract protected void parseFirstPage(Page page);

    abstract protected void afterDownload(boolean isSuccess , CodeInfo codeInfo);

    private boolean downloadZip(String filePath, String downloadPath,String host,String referer,String userAgent){
        Map<String, String> requestHeaderMap;
        requestHeaderMap = HeaderUtils.initGetHeaders(host, referer, userAgent);
        JsoupRequestData jsoupRequestData = new JsoupRequestData();
        jsoupRequestData.setMaxSize(100*1024*1024); //100MB
        jsoupRequestData.setHeaders(requestHeaderMap);
        jsoupRequestData.setTimeOut(0);  //设置在下载底层实现中无限等待
        jsoupRequestData.setFiddlerProxy();
        Future<Boolean> f = executorService.submit(new DownloadTask(filePath,downloadPath,jsoupRequestData));
        try{
            return f.get(5, TimeUnit.MINUTES);   //外部设置等待五分钟
        }catch (Exception e){
            logger.error("文件下载超时，下载路径：{}", downloadPath);
        }
        return false;
    }

    abstract protected Triplet<String,String, CodeInfo> parseNextPage(Page page);

    abstract protected void checkFinished(Page page);

    private class DownloadTask implements Callable<Boolean> {

        private String downloadPath;

        private JsoupRequestData jsoupRequestData;

        private String filePath;

        DownloadTask(String filePath, String downloadPath, JsoupRequestData jsoupRequestData) {
            this.downloadPath = downloadPath;
            this.jsoupRequestData = jsoupRequestData;
            this.filePath = filePath;
        }



        @Override
        public Boolean call() {
            logger.info("downloading file {}", downloadPath);
            long timestart = System.currentTimeMillis();

            byte[] binaryData = HttpsUtils.getBytes(downloadPath, jsoupRequestData, null);
            long timeend = System.currentTimeMillis();
            logger.info("download file complete {}, create file {}, 耗时：{}毫秒",downloadPath, filePath, timeend - timestart);
            if(binaryData == null){
                return false;
            }

            CreateFileUtil.generateFile(filePath, binaryData);
            logger.info("create file {} complete",filePath);
            return true;
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
