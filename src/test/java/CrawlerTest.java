import com.kxxfydj.crawler.Worker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by kxxfydj on 2018/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@WebAppConfiguration("src/main/resources")
public class CrawlerTest {
    @Autowired
    Worker worker;

    @Test
    public void startCralwer(){
        worker.start();
    }
}
