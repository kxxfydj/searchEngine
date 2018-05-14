package com.kxxfydj.crawler.gitlabInsert;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.CrawlerTypeEnum;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.crawler.InsertProcessor;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.NumberFormatUtil;
import com.kxxfydj.utils.RequestUtil;
import org.javatuples.Triplet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by kxxfydj on 2018/3/24.
 */
public class GitLabProcessor extends InsertProcessor {

    private AtomicInteger totalCount = new AtomicInteger(0);

    private AtomicInteger handlerdCount = new AtomicInteger(0);

    private List<CodeInfo> codeInfoList = new ArrayList<>();

    public GitLabProcessor(Site site, CrawlerTask crawlerTask) {
        super(site, crawlerTask);
        this.host = "gitlab.com";
        this.referer = null;
        this.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36";
    }

    @Override
    protected void parseFirstPage(Page page) {
        Document document = page.getHtml().getDocument();
        Elements items = document.select("#content-body > div.js-projects-list-holder > ul > li");

        boolean stop = false;
        for (Element item : items) {
            Element tagA = item.getElementsByTag("a").get(1);
            String link = tagA.attr("href");
            Element tagSpan = item.child(2).child(0);
            int stars = NumberFormatUtil.formatInt(tagSpan.text());
            if (stars < 200) {
                stop = true;
                break;
            }
            Request request = RequestUtil.createGetRequest(link, CommonTag.SECOND_PAGE);
            page.addTargetRequest(request);
            if (totalCount.incrementAndGet() >= crawlerTask.getFilterCount()) {
                return;
            }
        }
        if (!stop) {
            Element next = document.selectFirst("#content-body > div.js-projects-list-holder > div > ul > li > a");
            String nextLink = next.attr("href");
            Request request = RequestUtil.createGetRequest(nextLink, CommonTag.FIRST_PAGE);
            page.addTargetRequest(request);
        }
    }

    @Override
    protected Triplet<String, String, CodeInfo> parseNextPage(Page page) {
        if (page == null) {
            return null;
        }

        Document document = page.getHtml().getDocument();
        int stars = NumberFormatUtil.formatInt(document.selectFirst("#content-body > div.project-home-panel.text-center > div > div.project-repo-buttons > div > div > span.count").text());
        String gitPath = document.select("#project_clone").attr("value");
        String projectName = document.select("#content-body > div.project-home-panel.text-center > div > h1").text();
        String description = document.select("#content-body > div.project-home-panel.text-center > div > div.project-home-desc > p").text();
        String downloadPath = document.select("#tree-holder > div.nav-block > div.tree-controls > div > ul > li:nth-child(2) > a").attr("href");
        String author = document.select("body > div > div.content-wrapper > div.alert-wrapper > nav > div > div > ul > li:nth-child(1) > a").text();

        CodeInfo codeInfo = new CodeInfo();
        codeInfo.setStars(stars);
        codeInfo.setGitPath(gitPath);
        codeInfo.setProjectName(projectName);
        codeInfo.setDescription(description);
        codeInfo.setAuthor(author);
        codeInfo.setRepository(crawlerTask.getRepository());

        String filePath = this.filePath + File.separator + projectName + File.separator + projectName + ".zip";
        codeInfo.setFilePath(filePath);

        return new Triplet<>(filePath, downloadPath, codeInfo);
    }

    @Override
    protected void afterDownload(boolean isSuccess, CodeInfo codeInfo) {
        if (isSuccess) {
            codeInfoList.add(codeInfo);
        }
        handlerdCount.incrementAndGet();
    }

    @Override
    protected void checkFinished(Page page) {
        logger.info("thread:{} totalCount:{} handleredCount:{}", Thread.currentThread(), totalCount.get(), handlerdCount.get());
        if (totalCount.get() == handlerdCount.get()) {
            page.putField(PipelineKeys.CODEINFO_LIST, codeInfoList);
            page.putField(PipelineKeys.FINISHED, true);
        }
    }
}
