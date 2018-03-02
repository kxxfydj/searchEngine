package com.kxxfydj.crawlerConfig;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 当涉及到分页URL是完全一样(页码是通过post等参数提交)的时候,使用本扩展类进行分页抓取
 * 1.针对一直可以往下点下一页的URL(比如只有2页,但可以请求3、4页)这种情况,应当在对应的解析做好终止条件
 *   比如可以记录上一页解析到的第一条记录或者最后一条记录,如果当前页还是一样的记录,就终止添加分页URL
 * 2.针对有总页码的请求,循环到总页码最后一个记录,终止添加分页的URL
 * 扩展QueueScheduler，添加request时全部不进行URL过滤操作
 * Created by wangyonghuang on 2016/8/9.
 */
public class MyQueueScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler {
	
	private BlockingQueue<Request> queue = new LinkedBlockingQueue<>();

    @Override
	public void push(Request request, Task task) {
    	logger.debug("push to queue {}", request.getUrl());
    	queue.add(request);
	}

	@Override
    public synchronized Request poll(Task task) {
        return queue.poll();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        return queue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return getDuplicateRemover().getTotalRequestsCount(task);
    }

}
