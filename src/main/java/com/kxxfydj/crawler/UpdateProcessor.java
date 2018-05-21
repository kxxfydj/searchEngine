package com.kxxfydj.crawler;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.FileSupportEnum;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.HeaderUtils;
import com.kxxfydj.utils.HttpsUtils;
import com.kxxfydj.utils.JsoupRequestData;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by kxxfydj on 2018/5/12.
 */
public abstract class UpdateProcessor implements PageProcessor {

    protected static final String BRANCH = "branch";

    protected String host;

    protected String referer;

    protected String userAgent;

    protected List<CodeInfo> codeInfoList;

    protected CrawlerTask crawlerTask;

    protected CrawlerConfig crawlerConfig;

    protected List<CodeContent> codeContentList = Collections.synchronizedList(new ArrayList<>());

    public UpdateProcessor(List<CodeInfo> codeInfoList, CrawlerTask crawlerTask, CrawlerConfig crawlerConfig) {
        this.codeInfoList = codeInfoList;
        this.crawlerTask = crawlerTask;
        this.crawlerConfig = crawlerConfig;
    }

    @Override
    public void process(Page page) {
        String type = (String) page.getRequest().getExtra(CommonTag.TYPE);
        if (CommonTag.FIRST_PAGE.equals(type)) {
            processProjectPage(page);
        } else if (CommonTag.SECOND_PAGE.equals(type)) {
            processCommitPage(page);
        } else if (CommonTag.THIRD_PAGE.equals(type)) {
            processHashPage(page);
        } else if (CommonTag.FORTH_PAGE.equals(type)) {
            processFilePage(page);
        }
    }

    protected abstract String processProjectPage(Page page);

    protected abstract void processCommitPage(Page page);

    protected abstract void processHashPage(Page page);

    private void processFilePage(Page page) {
        String url = getFileRawUrl(page);

        Map<String, String> requestHeaderMap;
        requestHeaderMap = HeaderUtils.initGetHeaders(this.host, this.referer, this.userAgent);
        JsoupRequestData jsoupRequestData = new JsoupRequestData();
        jsoupRequestData.setMaxSize(100 * 1024); //100KB
        jsoupRequestData.setHeaders(requestHeaderMap);
        jsoupRequestData.setTimeOut(0);  //设置在下载底层实现中无限等待
//        jsoupRequestData.setFiddlerProxy();
        String fileContent = HttpsUtils.get(url, jsoupRequestData, null);

        generateCodeContent(page, fileContent);
        afterGenerateContentCode();
        checkFinish(page);
    }
    protected abstract String getFileRawUrl(Page page);

    protected abstract void generateCodeContent(Page page,String fileContent);

    protected abstract void afterGenerateContentCode();

    protected abstract void checkFinish(Page page);
}
