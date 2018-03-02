package com.kxxfydj.crawlerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;

public class CrawlerListener implements SpiderListener {

	private static final Logger logger = LoggerFactory.getLogger(CrawlerListener.class);

	private Spider spider;
	
	public CrawlerListener( Spider spider){
		this.spider = spider;
	}
	
	public void setSpider(Spider spider){
		this.spider = spider;
	}
	
	@Override	
	public void onSuccess(Request request) {
		logger.info("crawler success!");
	}

	@Override
	public void onError(Request request) {
		if (request == null) {
			logger.info("crawler error! request is null");
		} else if(spider != null) {
			//此处的停止并不一定会成功,但不管怎样,只要spider某个URL爬取出现异常,我们将通知终端此次爬取失败
			spider.stop();
			logger.info("crawler error! spider stop!");
		} else {
			logger.info("crawler error!");
		}
	}

}
