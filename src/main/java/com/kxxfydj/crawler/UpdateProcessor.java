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
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kxxfydj on 2018/5/12.
 */
public abstract class UpdateProcessor implements PageProcessor {

    protected String host;

    protected String referer;

    protected String userAgent;

    private List<CodeInfo> codeInfoList;

    private CrawlerTask crawlerTask;

    private CrawlerConfig crawlerConfig;

    protected List<CodeContent> codeContentList = Collections.synchronizedList(new ArrayList<>());

    public UpdateProcessor(List<CodeInfo> codeInfoList, CrawlerTask crawlerTask, CrawlerConfig crawlerConfig) {
        this.codeInfoList = codeInfoList;
        this.crawlerTask = crawlerTask;
        this.crawlerConfig = crawlerConfig;
    }

    @Override
    public void process(Page page) {
        String type = (String) page.getRequest().getExtra(CommonTag.TYPE);
        if(CommonTag.FIRST_PAGE.equals(type)){
            processCommitPage(page);
        }else if(CommonTag.SECOND_PAGE.equals(type)){
            processHashPage(page);
        }else if(CommonTag.THIRD_PAGE.equals(type)){
            processFilePage(page);
        }
    }

    protected abstract void processCommitPage(Page page);

    protected abstract void processHashPage(Page page);

    protected abstract String getFileRawUrl(Page page);

    private void processFilePage(Page page){
        String url = getFileRawUrl(page);
        generateCodeContent(page.getHtml().getDocument(),url);
        checkFinish(page);
    }

    protected abstract void afterGenerateContentCode();

    private void generateCodeContent(Document document, String url){
        Map<String, String> requestHeaderMap;
        requestHeaderMap = HeaderUtils.initGetHeaders(this.host, this.referer, this.userAgent);
        JsoupRequestData jsoupRequestData = new JsoupRequestData();
        jsoupRequestData.setMaxSize(100 * 1024); //100KB
        jsoupRequestData.setHeaders(requestHeaderMap);
        jsoupRequestData.setTimeOut(0);  //设置在下载底层实现中无限等待
        jsoupRequestData.setFiddlerProxy();
        String fileContent = HttpsUtils.get(url, jsoupRequestData, null);

        Element filePathDiv = document.getElementById("blob-path");
        String filePath = filePathDiv.text();
        String codeContentPath = this.crawlerConfig.getCodeunzipPath() + File.separator + crawlerTask.getRepository() + File.separator + filePath;

        String projectName = document.selectFirst("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div > h1 > strong > a").text();
        CodeInfo codeInfo = codeInfoList.stream()
                .filter(element -> element.getProjectName().equalsIgnoreCase(projectName))
                .collect(Collectors.toList())
                .get(0);

        FileSupportEnum language = FileSupportEnum.getLanguage(filePath);

        if (Objects.equals(language, FileSupportEnum.UnsupportFile)) {
            return;
        }

        CodeContent codeContent = new CodeContent();
        codeContent.setLanguage(language.getLanguage());
        codeContent.setBody(fileContent);
        codeContent.setPath(codeContentPath);
        codeContent.setFatherPath(codeContentPath.substring(0, codeContentPath.lastIndexOf("/")));
        codeContent.setCodeInfoId(codeInfo.getId());
        codeContent.setEnabled(true);

        codeContentList.add(codeContent);

    }

    protected abstract void checkFinish(Page page);

    @Override
    public Site getSite() {
        return null;
    }
}
