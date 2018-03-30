import com.kxxfydj.crawler.Worker;
import com.kxxfydj.crawler.xiciProxy.XiciCrawler;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.Proxy;
import com.kxxfydj.proxy.ProxyCenter;
import com.kxxfydj.proxy.ProxyCheck;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.ProxyService;
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
    RedisUtil<String, Proxy> redisUtil;

    @Autowired
    ProxyCenter proxyCenter;

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
    }

    @Test
    public void crawlerProxy() {
        XiciCrawler xiciCrawler = new XiciCrawler();
        xiciCrawler.setCrawlerConfig(crawlerConfig);
//        new Thread(xiciCrawler).start();
        xiciCrawler.run();
    }

    @Test
    public void redisMysqlTest() {
//        List<Proxy> proxyList = proxyService.getProxiesEnabled();

//        redisUtil.lSet("proxyList",proxyList);

        List<Proxy> proxies = redisUtil.lGet("proxyList", 0, -1);
        System.out.println(proxies);
    }

    @Test
    public void testCheckProxy() {
        ProxyCheck proxyCheck = new ProxyCheck(proxyService,proxyCenter);
        proxyCheck.checkProxiesFromDatabase();
    }
}
