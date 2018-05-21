import com.kxxfydj.entity.Proxy;
import com.kxxfydj.service.CodeInfoService;
import com.kxxfydj.service.ProxyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxfydj on 2018/4/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration("src/main/resources")
public class DatabaseTest {
    @Autowired
    CodeInfoService codeInfoService;

    @Autowired
    ProxyService proxyService;

    @Test
    public void test(){
        Proxy proxy = new Proxy();
        proxy.setIp("1.119.193.36");
        proxy.setPort(8080);
        proxy.setType("http");
        proxy.setEnabled(true);
        proxy.setUsedTimes(10);

        Proxy proxy2 = new Proxy();
        proxy2.setIp("1.196.158.132");
        proxy2.setPort(61234);
        proxy2.setType("http");
        proxy2.setSpeed(19170);
        proxy2.setEnabled(false);
        proxy2.setUsedTimes(14);

        List<Proxy> proxyList = new ArrayList<>();
        proxyList.add(proxy);
        proxyList.add(proxy2);
        System.out.println(proxyService.updateProxies(proxyList));
    }
}
