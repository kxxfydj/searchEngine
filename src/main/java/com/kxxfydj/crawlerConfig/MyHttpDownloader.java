package com.kxxfydj.crawlerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * create by kaiming_xu on 2017/9/3
 */
public class MyHttpDownloader extends HttpClientDownloader {

    private static Logger logger = LoggerFactory.getLogger(MyHttpDownloader.class);

    @Override
    public Page download(Request request, Task task) {
        logger.info("downloading page : {}" ,request.getUrl());
        return super.download(request, task);
    }
}
