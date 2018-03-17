package com.kxxfydj.crawlerConfig;

import com.kxxfydj.common.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.concurrent.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 扩展ThreadPoolExecutor
 * @author wangyonghuang
 *
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor {
	private static final Logger logger = getLogger(MyThreadPoolExecutor.class);
	
	public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void afterExecute(Runnable runnable, Throwable throwable) {
		Throwable myThrowable = null;
		if (throwable == null && runnable instanceof Future<?>) {
			try {
				((Future<?>) runnable).get();
			} catch (CancellationException ce) {
				myThrowable = ce;
			} catch (ExecutionException ee) {
				myThrowable = ee.getCause();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
		if (myThrowable != null){
			logger.error(myThrowable.getMessage(), myThrowable);
		}
		//注:针对登录成功后使用webmagic爬取用户数据的,并不会等真正爬取完成后才走到这里
		//   因为webmagic等于又开了线程，spider.start()后就会执行到这里

		super.afterExecute(runnable, throwable);
		//线程使用完后把MDC及ThreadContext清理掉，防止下次使用时出现赃数据
//		MDC.clear();
		ThreadContext.clear();
	}
}