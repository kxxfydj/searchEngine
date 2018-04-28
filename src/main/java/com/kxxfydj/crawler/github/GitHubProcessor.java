package com.kxxfydj.crawler.github;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.CrawlerTypeEnum;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.crawler.CodeProcessor;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.entity.CrawlerTask;
import com.kxxfydj.utils.NumberFormatUtil;
import com.kxxfydj.utils.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class GitHubProcessor extends CodeProcessor {

    private List<CodeInfo> codeInfoList = new ArrayList<>();

    private AtomicLong totalCount = new AtomicLong(0L);

    private AtomicLong handleredCount = new AtomicLong(0L);

    private boolean hasNext = false;

    public GitHubProcessor(Site site, CrawlerTask crawlerTask) {
        super(site,crawlerTask);
        super.host = "codeload.github.com";
        super.referer = "https://github.com";
        super.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36";
    }

    @Override
    protected void parseFirstPage(Page page) {
        if (page == null) {
            return;
        }
        Document document = page.getHtml().getDocument();
        Elements repoList = document.select("#js-pjax-container > div > div.columns > div.column.three-fourths.codesearch-results > div > ul > div");
        for (Element repo : repoList) {
            Element codeLinkTag = repo.getElementsByTag("a").first();
            String codeLink = codeLinkTag.attr("href");
            String language = repo.child(1).text();
            Request request = RequestUtil.createGetRequest(codeLink, CommonTag.NEXT_PAGE);
            request.putExtra("language",language);
            synchronized (totalCount) {
                if (totalCount.get() <= crawlerTask.getFilterCount()) {
                    totalCount.incrementAndGet();
                    page.addTargetRequest(request);
                } else {
                    hasNext = false;
                    return;
                }
            }
        }
        Element nextPage = document.selectFirst("#js-pjax-container > div > div.columns > div.column.three-fourths.codesearch-results > div > div.paginate-container > div > a.next_page");
        String nextUrl = nextPage.attr("href");
        if(StringUtils.isNotBlank(nextUrl)){
            hasNext = true;
            Request request = RequestUtil.createGetRequest(nextUrl,CommonTag.FIRST_PAGE);
            page.addTargetRequest(request);
        }else{
            hasNext = false;
        }
    }

    @Override
    protected Triplet<String, String, CodeInfo> parseNextPage(Page page) {
        if (page == null) {
            return null;
        }
        String language = (String) page.getRequest().getExtra("language");

        Document document = page.getHtml().getDocument();
        String projectName = document.selectFirst("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div > h1 > strong > a").text();
        String author = document.select("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div > h1 > span.author > a").first().text();
        String description = document.select("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.js-repo-meta-container > div.repository-meta.mb-0.js-repo-meta-edit.js-details-container > div > span").first().text();
        int stars = NumberFormatUtil.formatInt(document.select("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div > ul > li:nth-child(2) > a.social-count.js-social-count").text());
        String gitPath = document.select("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.file-navigation.in-mid-page > details > div > div > div.get-repo-modal-options > div.clone-options.https-clone-options > div > input").first().attr("value");
        String downloadPath = document.select("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.file-navigation.in-mid-page > details > div > div > div.get-repo-modal-options > div.mt-2 > a").first().attr("href");

        CodeInfo codeInfo = new CodeInfo();
        codeInfo.setDescription(description);
        codeInfo.setLanguage(language);
        codeInfo.setProjectName(projectName);
        codeInfo.setAuthor(author);
        codeInfo.setStars(stars);
        codeInfo.setRepository(CrawlerTypeEnum.GITHUB.getType());
        codeInfo.setGitPath(gitPath);

        String filePath = this.filePath + File.separator + projectName + File.separator + projectName + ".zip";
        codeInfo.setFilePath(filePath);

        return new Triplet<>(filePath, downloadPath, codeInfo);
    }

    @Override
    protected void afterDownload(boolean isSuccess,CodeInfo codeInfo) {
        if(isSuccess){
            codeInfoList.add(codeInfo);
        }
        handleredCount.incrementAndGet();
    }

    @Override
    protected void checkFinished(Page page) {
        logger.info("thread:{} totalCount:{} handleredCount:{}",Thread.currentThread(), totalCount.get() ,handleredCount.get());
        if (totalCount.get() == handleredCount.get() && !hasNext) {
            page.putField(PipelineKeys.CRAWLER_TYPE, CrawlerTypeEnum.GITHUB.getType());
            page.putField(PipelineKeys.CODEINFO_LIST, codeInfoList);
            page.putField(PipelineKeys.FINISHED, true);
        }
    }

}
