import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.crawler.Worker;
import com.kxxfydj.crawler.xiciProxy.XiciCrawler;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.Proxy;
import com.kxxfydj.proxy.ProxyCenter;
import com.kxxfydj.proxy.ProxyCheck;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.ProxyService;
import com.kxxfydj.task.CheckProxyTask;
import com.kxxfydj.task.CrawlerCodeTask;
import com.kxxfydj.task.CrawlerProxyTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration("src/main/resources")
public class CrawlerTest {
    @Autowired
    Worker worker;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ProxyCenter proxyCenter;

    @Autowired
    CrawlerCodeTask task;

    @Autowired
    CrawlerProxyTask crawlerProxyTask;

    @Autowired
    CheckProxyTask checkProxyTask;

    @Autowired
    private CrawlerConfig crawlerConfig;

    @Autowired
    private ProxyService proxyService;

    @Test
    public void startCralwer() {
//        ContextLoader.getCurrentWebApplicationContext();
//        ProxyCenter proxyCenter = ApplicationContextUtils.getBean(ProxyCenter.class);
//        worker.start();
//        redisUtil.lSet("testKey","testValue");
//        redisUtil.set("kaikai","name");
        task.updateCode();
    }

    @Test
    public void crawlerProxy() {
//        crawlerProxyTask.crawlerProxy();
        checkProxyTask.checkDatabase();
    }

    @Test
    public void redisMysqlTest() {
//        List<Proxy> proxyList = proxyService.getProxiesEnabled();

//        redisUtil.lSet("proxyList",proxyList);

        List<Proxy> proxies = redisUtil.lGet(RedisKeys.PROXYLIST.getKey(), 0, -1);
        System.out.println(proxies);
    }

    @Test
    public void testCheckProxy() {
        ProxyCheck proxyCheck = new ProxyCheck(proxyService,proxyCenter);
        proxyCheck.checkProxiesFromDatabase();
    }
}
