import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.crawler.Worker;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.Proxy;
import com.kxxfydj.proxy.ProxyCenter;
import com.kxxfydj.proxy.ProxyCheck;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.ProxyService;
import com.kxxfydj.task.CheckProxyTask;
import com.kxxfydj.task.CrawlerCodeTask;
import com.kxxfydj.task.CrawlerProxyTask;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
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

    @Test
    public void testGitRep() throws Exception{
        File localPath = new File("D:\\codeSource\\test");
        if(!localPath.delete()) {
            System.out.println("删除文件失败");
        }

        long startTime = System.currentTimeMillis();
        // then clone
        System.out.println("Cloning from " + "https://gitlab.com/gitlab-org/gitlab-runner.git" + " to " + localPath);
        try (Git result = Git.cloneRepository()
                .setURI("https://gitlab.com/gitlab-org/gitlab-runner.git")
                .setDirectory(localPath)
                .call()) {
            long endTime = System.currentTimeMillis();
            // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
            System.out.println("Having repository: " + result.getRepository().getDirectory()+ " 共耗时 " + (endTime - startTime) + "毫秒");
        }

        // clean up here to not keep using more and more disk-space for these samples
//        FileUtils.deleteDirectory(localPath);
    }
}
