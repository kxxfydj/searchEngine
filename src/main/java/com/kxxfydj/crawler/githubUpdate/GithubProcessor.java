package com.kxxfydj.crawler.githubUpdate;

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
public class GithubProcessor extends UpdateProcessor {

    private Site site;

    private AtomicInteger totalCount = new AtomicInteger(0);

    private AtomicInteger handleredCount = new AtomicInteger(0);

    public GithubProcessor(Site site, List<CodeInfo> codeInfoList, CrawlerConfig crawlerConfig, CrawlerTask crawlerTask) {
        super(codeInfoList, crawlerTask, crawlerConfig);
        this.site = site;
        super.host = "codeload.github.com";
        super.referer = "https://github.com";
        super.referer = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36";
    }

    @Override
    protected String processProjectPage(Page page) {
        Document document = page.getHtml().getDocument();

        Element linkTagA = document.selectFirst("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.overall-summary.overall-summary-bottomless > div > div > ul > li.commits > a");
        String link = linkTagA.attr("href");
        Element branchTag = document.selectFirst("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.file-navigation.in-mid-page > div.select-menu.branch-select-menu.js-menu-container.js-select-menu.float-left > button > span");
        String branch = branchTag.text();
        Request request = RequestUtil.createGetRequest(link, CommonTag.SECOND_PAGE);
        request.putExtra(UpdateProcessor.BRANCH,branch);
        page.addTargetRequest(request);
        return branch;
    }

    @Override
    protected void processCommitPage(Page page) {
        Document document = page.getHtml().getDocument();
        String branch = (String) page.getRequest().getExtra(UpdateProcessor.BRANCH);
        Element commitsDiv = document.selectFirst("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.commits-listing.commits-listing-padded.js-navigation-container.js-active-navigation-container");
        Elements lastCommits = commitsDiv.getElementsByTag("ol").first().children();

        for (Element lastCommit : lastCommits) {
            Element a = lastCommit.selectFirst("a.sha.btn.btn-outline.BtnGroup-item");
            String url = a.attr("href");
            Request request = RequestUtil.createGetRequest(url, CommonTag.THIRD_PAGE);
            request.putExtra(UpdateProcessor.BRANCH,branch);
            page.addTargetRequest(request);
        }
    }

    @Override
    protected void processHashPage(Page page) {
        Document document = page.getHtml().getDocument();
        String branch = (String) page.getRequest().getExtra(UpdateProcessor.BRANCH);
        Elements files = document.select("#files > div > div");

        for (Element file : files) {
            Element a = file.selectFirst("div.file-header.js-file-header > div.file-actions > a");
            String url = a.attr("href");
            totalCount.incrementAndGet();
            Request request = RequestUtil.createGetRequest(url, CommonTag.FORTH_PAGE);
            request.putExtra(UpdateProcessor.BRANCH,branch);
            page.addTargetRequest(request);
        }
    }


    @Override
    protected String getFileRawUrl(Page page) {
        Document document = page.getHtml().getDocument();
        Element rowLink = document.getElementById("raw-url");
        return rowLink.attr("href");
    }


    @Override
    protected void generateCodeContent(Page page, String fileContent) {
        Document document = page.getHtml().getDocument();

        Element filePathDiv = document.getElementById("blob-path");
        String filePath = filePathDiv.text().replaceAll("/", Matcher.quoteReplacement(File.separator));

        String branch = (String) page.getRequest().getExtra(UpdateProcessor.BRANCH);
        String codeContentPath = generateFilePath(filePath,branch);

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
        codeContent.setFatherPath(codeContentPath.substring(0, codeContentPath.lastIndexOf(File.separator)));
        codeContent.setCodeInfoId(codeInfo.getId());
        codeContent.setEnabled(true);

        codeContentList.add(codeContent);
    }

    protected String generateFilePath(String originPath, String branch) {
        String projectName = originPath.substring(0,originPath.indexOf(File.separator));
        String filePath = projectName+ File.separator + projectName + "-" + branch + originPath.substring(originPath.indexOf(File.separator),originPath.length());
        return super.crawlerConfig.getCodeunzipPath() + File.separator + crawlerTask.getRepository() + File.separator + filePath;
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
