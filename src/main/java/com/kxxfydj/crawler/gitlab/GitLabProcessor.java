package com.kxxfydj.crawler.gitlab;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.CrawlerTypeEnum;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.crawler.CodeProcessor;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.NumberFormatUtil;
import com.kxxfydj.utils.RequestUtil;
import org.javatuples.Pair;
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
public class GitLabProcessor extends CodeProcessor {

    private AtomicInteger totalCount = new AtomicInteger(0);

    private AtomicInteger handlerdCount = new AtomicInteger(0);

    private List<CodeInfo> codeInfoList = new ArrayList<>();

    public GitLabProcessor(Site site, CrawlerTask crawlerTask) {
        super(site,crawlerTask);
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
            Element tagSpan = item.child(2).child(1);
            int stars = NumberFormatUtil.formatInt(tagSpan.text());
            if (stars < 10) {
                stop = true;
                break;
            }
            totalCount.incrementAndGet();
            Request request = RequestUtil.createGetRequest(link, CommonTag.NEXT_PAGE);
            page.addTargetRequest(request);
        }
        if (!stop) {
            Element next = document.selectFirst("#content-body > div.js-projects-list-holder > div > ul > li > a");
            String nextLink = next.attr("href");
            Request request = RequestUtil.createGetRequest(nextLink, CommonTag.FIRST_PAGE);
            page.addTargetRequest(request);
        }
    }

    @Override
    protected Pair<String, String> parseNextPage(Page page) {
        if (page == null) {
            return null;
        }

        Document document = page.getHtml().getDocument();
        int stars = NumberFormatUtil.formatInt(document.selectFirst("#content-body > div.project-home-panel.text-center > div > div.project-repo-buttons > div > div > span.count").text());
        String gitPath = document.select("#project_clone").attr("value");
        String projectName = document.select("#content-body > div.project-home-panel.text-center > div > h1").text();
        String description = document.select("#content-body > div.project-home-panel.text-center > div > div.project-home-desc > p").text();
        String downloadPath = document.select("#tree-holder > div.nav-block > div.tree-controls > div > ul > li:nth-child(2) > a").attr("href");

        CodeInfo codeInfo = new CodeInfo();
        codeInfo.setStars(stars);
        codeInfo.setGitPath(gitPath);
        codeInfo.setProjectName(projectName);
        codeInfo.setDescription(description);
        codeInfoList.add(codeInfo);

        String filePath = this.filePath + File.separator + projectName + File.separator + projectName + ".zip";
        codeInfo.setFilePath(filePath);

        handlerdCount.incrementAndGet();
        return new Pair<>(filePath,downloadPath);
    }

    @Override
    protected void checkFinished(Page page) {
        if(totalCount.get() == handlerdCount.get()){
            page.putField(PipelineKeys.CODEINFO_LIST,codeInfoList);
            page.putField(PipelineKeys.CRAWLER_TYPE, CrawlerTypeEnum.GITLAB.getType());
            page.putField(PipelineKeys.FINISHED,true);
        }
    }
}
