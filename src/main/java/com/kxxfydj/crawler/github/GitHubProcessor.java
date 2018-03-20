package com.kxxfydj.crawler.github;

import com.kxxfydj.common.CommonTag;
import com.kxxfydj.common.CrawlerTypeEnum;
import com.kxxfydj.common.PipelineKeys;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.utils.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class GitHubProcessor implements PageProcessor {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    String PROJECT_PATH = System.getProperty("user.dir");

    private static final String HOST = "codeload.github.com";

    private static final String REFERER = "https://github.com";

    private static final String USERAGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36";

    private static Logger logger = LoggerFactory.getLogger(GitHubProcessor.class);

    private Site site;

    private String language;

    private List<CodeInfo> codeInfoList = new ArrayList<>();

    private AtomicInteger pageCount = new AtomicInteger(0);

    private AtomicLong totalCount = new AtomicLong(0L);

    private AtomicLong handleredCount = new AtomicLong(0L);

    public GitHubProcessor(Site site, String language) {
        this.site = site;
        this.language = language;
    }

    @Override
    public void process(Page page) {
        String type = (String) page.getRequest().getExtra(CommonTag.TYPE);

        if (CommonTag.FIRST_PAGE.equals(type)) {
            processFirstPage(page);
        } else if (CommonTag.NEXT_PAGE.equals(type)) {
            processNextPage(page);
        }
    }

    private void processFirstPage(Page page) {
        Document document = page.getHtml().getDocument();
        Elements repoList = document.select("#js-pjax-container > div > div.columns > div.column.three-fourths.codesearch-results > div > ul > div");
        for (Element repo : repoList) {
            Element codeLinkTag = repo.getElementsByTag("a").first();
            String codeLink = codeLinkTag.attr("href");
            Request request = RequestUtil.createGetRequest(codeLink, CommonTag.NEXT_PAGE);
            totalCount.incrementAndGet();
            page.addTargetRequest(request);
        }
        Element nextPage = document.selectFirst("#js-pjax-container > div > div.columns > div.column.three-fourths.codesearch-results > div > div.paginate-container > div > a.next_page");
        String nextUrl = nextPage.attr("href");
        pageCount.incrementAndGet();
//        if(StringUtils.isNotBlank(nextUrl)){
//            Request request = RequestUtil.createGetRequest(nextUrl,CommonTag.FIRST_PAGE);
//            page.addTargetRequest(request);
//        }else
        if (totalCount == handleredCount) {
            page.putField(PipelineKeys.CRAWLER_TYPE, CrawlerTypeEnum.GITHUB.getType());
            page.putField(PipelineKeys.LANGUAGE, this.language);
            page.putField(PipelineKeys.CODEINFO_LIST, codeInfoList);
            page.putField(PipelineKeys.FINISHED, true);
        }
    }

    /**
     * @param page
     */
    private void processNextPage(Page page) {
        Document document = page.getHtml().getDocument();
        String projectName = document.select("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div > h1 > span.author > a").first().text();
        String language = document.select("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div > h1 > strong > a").first().text();
        String description = document.select("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.js-repo-meta-container > div.repository-meta.mb-0.js-repo-meta-edit.js-details-container > div > span").first().text();
        int stars = NumberFormatUtil.formatInt(document.select("#js-repo-pjax-container > div.pagehead.repohead.instapaper_ignore.readability-menu.experiment-repo-nav > div > ul > li:nth-child(2) > a.social-count.js-social-count").text());
        String gitPath = document.select("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.file-navigation.in-mid-page > details > div > div > div.get-repo-modal-options > div.clone-options.https-clone-options > div > input").first().attr("value");
        String downloadPath = document.select("#js-repo-pjax-container > div.container.new-discussion-timeline.experiment-repo-nav > div.repository-content > div.file-navigation.in-mid-page > details > div > div > div.get-repo-modal-options > div.mt-2 > a").first().attr("href");

        CodeInfo codeInfo = new CodeInfo();
        codeInfo.setDescription(description);
        codeInfo.setLanguage(language);
        codeInfo.setProjectName(projectName);
        codeInfo.setStars(stars);
        codeInfo.setRepository("github");
        codeInfo.setGitPath(gitPath);

        codeInfoList.add(codeInfo);
        handleredCount.incrementAndGet();
        downloadZip(language, projectName, downloadPath);
    }

    private void downloadZip(String language, String projectName, String downloadPath) {
        downloadPath = downloadPath.replaceAll("archive", "zip");
        downloadPath = downloadPath.substring(0, downloadPath.lastIndexOf(".zip"));
        downloadPath = downloadPath.replaceAll("github\\.com", "codeload.github.com");
        String filePath = PROJECT_PATH + FILE_SEPARATOR + "github" + FILE_SEPARATOR + language + FILE_SEPARATOR + projectName;
//        File file = new File(filePath);

        Map<String, String> requestHeaderMap;
        requestHeaderMap = HeaderUtils.initGetHeaders(HOST, REFERER, USERAGENT);
        JsoupRequestData jsoupRequestData = new JsoupRequestData();
        jsoupRequestData.setProxyFromRedis();
        jsoupRequestData.setHeaders(requestHeaderMap);
//            apacheHttpRequestData.setFiddlerProxy();

        logger.info("downloading file {}", downloadPath);
        byte[] binaryData = HttpsUtils.getBytes(downloadPath, jsoupRequestData, null);
        CreateFileUtil.generateFile(filePath, binaryData);
    }

    @Override
    public Site getSite() {
        return this.site;
    }
}
