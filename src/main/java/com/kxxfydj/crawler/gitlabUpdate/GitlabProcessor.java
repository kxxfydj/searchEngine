package com.kxxfydj.crawler.gitlabUpdate;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.FileSupportEnum;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.crawler.UpdateProcessor;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.RequestUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by kxxfydj on 2018/5/12.
 */
public class GitlabProcessor extends UpdateProcessor {
    private Site site;

    private AtomicInteger totalCount = new AtomicInteger(0);

    private AtomicInteger handleredCount = new AtomicInteger(0);

    public GitlabProcessor(Site site, List<CodeInfo> codeInfoList, CrawlerConfig crawlerConfig, CrawlerTask crawlerTask) {
        super(codeInfoList, crawlerTask, crawlerConfig);
        this.site = site;
        this.host = "gitlab.com";
        this.referer = null;
        this.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36";
    }

    @Override
    protected String processProjectPage(Page page) {
        Document document = page.getHtml().getDocument();
        String link = document.selectFirst("#content-body > nav > ul > li:nth-child(2) > a").attr("href");
        Element branckTag = document.selectFirst("#tree-holder > div.nav-block > div.tree-ref-container > div > form > div > button > span");
        String branch = branckTag.text();
        Request request = RequestUtil.createGetRequest(link,CommonTag.SECOND_PAGE);
        page.addTargetRequest(request);
        return branch;
    }

    @Override
    protected void processCommitPage(Page page) {
        Document document = page.getHtml().getDocument();
        Elements commitDivs = document.select("#commits-list > li:nth-child(2) > ul > li");

        for (Element commitDiv : commitDivs) {
            String time = commitDiv.getElementsByTag("time").first().text();
            if (time.matches("about \\d+ [hours|minutes|seconds] ago")) {
                Element a = commitDiv.selectFirst("a.commit-row-message.item-title");
                String url = a.attr("href");
                Request request = RequestUtil.createGetRequest(url, CommonTag.THIRD_PAGE);
                page.addTargetRequest(request);
            } else {
                break;
            }
        }
    }

    @Override
    protected void processHashPage(Page page) {
        Document document = page.getHtml().getDocument();
        Elements fileDivs = document.select("#content-body > div > div.files > div");

        for (Element fileDiv : fileDivs) {
            Element fileLinkTag = fileDiv.selectFirst("a.btn.view-file.js-view-file");
            if(fileLinkTag == null){
                continue;
            }
            String url = fileLinkTag.attr("href");
            Request request = RequestUtil.createGetRequest(url, CommonTag.FORTH_PAGE);
            this.totalCount.incrementAndGet();
            page.addTargetRequest(request);
        }

    }

    @Override
    protected String getFileRawUrl(Page page) {
        Document document = page.getHtml().getDocument();
        String link = document.selectFirst("#blob-content-holder > article > div.js-file-title.file-title-flex-parent > div.file-actions > div:nth-child(1) > a").attr("href");
        return link;
    }

    @Override
    protected void generateCodeContent(Page page, String fileContent) {
        Document document = page.getHtml().getDocument();

        Elements filePathLis = document.select("#tree-holder > div.nav-block > div.tree-ref-container > ul > li");
        StringBuilder path = new StringBuilder();
        for(int i = 0 ; i< filePathLis.size() - 1; i++){
            path.append(filePathLis.get(i).text() + File.separator);
        }
        String filePath = path.append(filePathLis.get(filePathLis.size() - 1)).toString();

        String branch = (String) page.getRequest().getExtra(UpdateProcessor.BRANCH);
        String codeContentPath = generateFilePath(filePath,branch);

        String projectName = document.selectFirst("body > div > div.content-wrapper > div.alert-wrapper > nav > div > div > ul > li:nth-child(2) > a > span").text();
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

    private String generateFilePath(String originPath,String branch) {
        return "";
    }

    @Override
    protected void afterGenerateContentCode() {
        //do nothing
    }

    @Override
    protected void checkFinish(Page page) {
        if (totalCount.get() == handleredCount.incrementAndGet()) {
            page.putField(PipelineKeys.CODECONTENT_LIST, codeContentList);
            page.putField(PipelineKeys.FINISHED, true);
        }
    }


    @Override
    public Site getSite() {
        return this.site;
    }
}
