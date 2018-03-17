package com.kxxfydj.crawlerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.lang.reflect.Field;

/**
 * create by kaiming_xu on 2017/9/3
 */
public class MyHttpClientDownloader extends HttpClientDownloader {

    private static Logger logger = LoggerFactory.getLogger(MyHttpClientDownloader.class);

    private MyHttpClientGenerator httpClientGenerator;

    public MyHttpClientDownloader() {
        try {
            Field httpClientGeneratorField = HttpClientDownloader.class.getDeclaredField("httpClientGenerator");
            httpClientGeneratorField.setAccessible(true);
            httpClientGenerator = new MyHttpClientGenerator();
            httpClientGeneratorField.set(this, httpClientGenerator);
        } catch (Exception e) {
            logger.error("Init MyHttpClientGenerator occur error! ", e);
        }
    }

    @Override
    public Page download(Request request, Task task) {
        logger.info("downloading page : {}" ,request.getUrl());
        return super.download(request, task);
    }
}
