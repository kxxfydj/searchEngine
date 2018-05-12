package com.kxxfydj.crawler.githubUpdate;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.FileSupportEnum;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.crawler.UpdateProcessor;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.HeaderUtils;
import com.kxxfydj.utils.HttpsUtils;
import com.kxxfydj.utils.JsoupRequestData;
import com.kxxfydj.utils.RequestUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by kxxfydj on 2018/5/12.
 */
public class GithubProcessor extends UpdateProcessor {

    private Site site;

    private AtomicInteger totalCount = new AtomicInteger(0);

    private AtomicInteger handleredCount = new AtomicInteger(0);

    public GithubProcessor(Site site, List<CodeInfo> codeInfoList, CrawlerConfig crawlerConfig, CrawlerTask crawlerTask) {
        super(codeInfoList,crawlerTask,crawlerConfig);
        this.site = site;
        super.host = "codeload.github.com";
        super.referer = "https://github.com";
        super.referer = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36";
    }

    @Override
    protected void processCommitPage(Page page) {
        Document document = page.getHtml().getDocument();
        Element commitsDiv = document.selectFirst("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.commits-listing.commits-listing-padded.js-navigation-container.js-active-navigation-container");
        Elements lastCommits = commitsDiv.getElementsByTag("ol").first().children();

        for (Element lastCommit : lastCommits) {
            Element a = lastCommit.selectFirst("a.sha.btn.btn-outline.BtnGroup-item]");
            String url = a.attr("href");
            totalCount.incrementAndGet();
            Request request = RequestUtil.createGetRequest(url, CommonTag.SECOND_PAGE);
            page.addTargetRequest(request);
        }
    }

    @Override
    protected void processHashPage(Page page) {
        Document document = page.getHtml().getDocument();
        Elements files = document.select("#files > div > div");

        for (Element file : files) {
            Element a = file.selectFirst("div.file-header.js-file-header > div.file-actions > a");
            String url = a.attr("href");
            totalCount.incrementAndGet();
            Request request = RequestUtil.createGetRequest(url, CommonTag.THIRD_PAGE);
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
    protected void afterGenerateContentCode() {
        //do nothing
    }

    @Override
    protected void checkFinish(Page page) {
        if(totalCount.get() == totalCount.incrementAndGet()){
            page.putField(PipelineKeys.CODECONTENT_LIST,codeContentList);
            page.putField(PipelineKeys.FINISHED,true);
        }
    }


    @Override
    public Site getSite() {
        return this.site;
    }
}
