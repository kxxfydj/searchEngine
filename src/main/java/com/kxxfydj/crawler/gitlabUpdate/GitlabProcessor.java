package com.kxxfydj.crawler.gitlabUpdate;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.crawler.UpdateProcessor;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.RequestUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    protected void processCommitPage(Page page) {
        Document document = page.getHtml().getDocument();
        Elements commitDivs = document.select("#commits-list > li:nth-child(2) > ul > li");

        for (Element commitDiv : commitDivs) {
            String time = commitDiv.getElementsByTag("time").first().text();
            if (time.matches("about \\d+ hours ago")) {
                Element a = commitDiv.selectFirst("a.commit-row-message.item-title");
                String url = a.attr("href");
                Request request = RequestUtil.createGetRequest(url, CommonTag.SECOND_PAGE);
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
            String url = fileLinkTag.attr("href");
            Request request = RequestUtil.createGetRequest(url, CommonTag.THIRD_PAGE);
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
    protected void afterGenerateContentCode() {
        //do nothing
    }

    @Override
    protected void checkFinish(Page page) {
        if (totalCount.get() == totalCount.incrementAndGet()) {
            page.putField(PipelineKeys.CODECONTENT_LIST, codeContentList);
            page.putField(PipelineKeys.FINISHED, true);
        }
    }


    @Override
    public Site getSite() {
        return this.site;
    }
}
