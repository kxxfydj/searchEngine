import com.kxxfydj.enginer.EngineConfig;
import com.kxxfydj.enginer.IndexManager;
import com.kxxfydj.enginer.SearchIndex;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.form.HitDocument;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.CodeContentService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/4/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration("src/main/resources")
public class engineTest {

    private static final Logger logger = LoggerFactory.getLogger(engineTest.class);

    @Autowired
    private RedisUtil<String,List<HitDocument>> redisUtil;

    @Autowired
    private EngineConfig engineConfig;

    @Autowired
    private CodeContentService codeContentService;

    @Autowired
    private SearchIndex searchIndex;

    @Test
    public void testIndexFile() {
        List<CodeContent> codeContentList = codeContentService.getAllFiles();
        IndexManager indexManager = new IndexManager(engineConfig);
        indexManager.createIndex(codeContentList);
    }

    @Test
    public void testQueryParser() {

        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try (
                FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\testIndex"));
                IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig)
        ) {
            Document document = new Document();
            document.add(new TextField("testFileName", "fileName", Field.Store.YES));
            document.add(new TextField("testPath", "path", Field.Store.YES));
            document.add(new TextField("testContent", "content", Field.Store.YES));

            indexWriter.addDocument(document);

            indexWriter.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testSearchIndex(){
        searchIndex.searchIndex(EngineConfig.FILENAME + ":java");
//        Map<String,List<HitDocument>> listMap = redisUtil.hmget("documentList");
//        System.out.println(listMap);
    }
}
