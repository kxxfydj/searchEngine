import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.service.UnzipService;
import com.kxxfydj.task.CrawlerCodeTask;
import com.kxxfydj.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
    UnzipService unzipService;

    @Test
    public void test(){
        FileUtils.unzipFiles(crawlerConfig.getCodezipPath(),crawlerConfig.getCodeunzipPath());
    }

    @Test
    public void testFileToDatabase(){
//        FileUtils.unzipFiles(crawlerConfig.getCodezipPath(),crawlerConfig.getCodeunzipPath());
        unzipService.fileToDatabase("github",crawlerConfig.getCodeunzipPath(),false);
    }

    @Test
    public void testPath() throws IOException{
        String[] files = "D:\\unzipDir\\github\\Java\\agileorbit-cookbooks\\agileorbit-cookbooks\\java-master\\recipes\\set_attributes_from_version.rb".split("\\\\");
        String path = "D:\\unzipDir\\github\\Java\\TheAlgorithms\\TheAlgorithms\\Java-master\\Misc";
        Path file = Paths.get(path,"heap_sort.java");
        int startindex = crawlerConfig.getCodeunzipPath().split("\\\\").length - 1;
        System.out.println(file.subpath(startindex,file.getNameCount()));
    }

    @Test
    public void fileToDataBase(){
        System.out.println("../searchResource/searchFile.html?filePath=gitlab\\gitlab-runner\\gitlab-runner-master\\vendor\\k8s.io\\kubernetes\\plugin\\pkg\\auth\\authenticator\\request\\union\\<strong>unionauth_test.go</strong>".replaceAll("</?strong>",""));
    }
}
