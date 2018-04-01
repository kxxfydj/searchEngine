import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.task.CrawlerCodeTask;
import com.kxxfydj.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by kxxfydj on 2018/4/1.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration("src/main/resources")
public class FileTest {
    @Autowired
    CrawlerConfig crawlerConfig;

    @Autowired
    CrawlerCodeTask codeTask;
    @Test
    public void test(){
        FileUtils.unzipFiles(crawlerConfig.getCodezipPath(),crawlerConfig);
    }

    @Test
    public void testFileToDatabase(){
        codeTask.fileToDatabase(crawlerConfig.getCodeunzipPath());
    }
}
