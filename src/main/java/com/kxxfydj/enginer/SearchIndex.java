package com.kxxfydj.enginer;

import com.kxxfydj.form.HitDocument;
import com.kxxfydj.redis.RedisUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/4/3.
 */
@Component
public class SearchIndex {
    private static final Logger logger = LoggerFactory.getLogger(SearchIndex.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EngineConfig engineConfig;

    private SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<strong>", "</strong>");

    private Fragmenter fragment = new NullFragmenter();

    private Analyzer analyzer = new StandardAnalyzer();

    public SearchIndex(EngineConfig engineConfig) {
        this.engineConfig = engineConfig;
    }

    public List<HitDocument> searchIndex(String clauses) {
        try (
                Directory directory = FSDirectory.open(Paths.get(engineConfig.getIndexFile()));
                DirectoryReader reader = DirectoryReader.open(directory)
        ) {
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            QueryParser queryParser = new QueryParser(EngineConfig.CONTENT, analyzer);
            Query query = queryParser.parse(clauses);

            QueryScorer scorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(simpleHtmlFormatter, scorer);
            highlighter.setTextFragmenter(fragment);
            ScoreDoc[] hits = indexSearcher.search(query, 1000).scoreDocs;

            List<HitDocument> documentList = handlerDocument(clauses, hits, indexSearcher, highlighter);
            redisUtil.lSet("documentList:" + clauses, documentList, 60 * 60);   //一小时过期
            logger.info("lucene查询字段结果已缓存 clause:{}  并设置一小时过期", clauses);

            return documentList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private List<HitDocument> handlerDocument(String clause, ScoreDoc[] hits, IndexSearcher indexSearcher, Highlighter highlighter) throws Exception {
        String luceneFiled = clause.split(":")[0];
        Class clazz = HitDocument.class;
        Field[] fields = clazz.getDeclaredFields();
        List<HitDocument> documentList = new ArrayList<>();

        for (ScoreDoc doc : hits) {
            Document hitDoc = indexSearcher.doc(doc.doc);
            HitDocument document = new HitDocument();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equalsIgnoreCase(luceneFiled.toLowerCase())) {
                    String fieldContent = StringEscapeUtils.escapeHtml(hitDoc.get(luceneFiled));
                    String bestFragment = highlighter.getBestFragment(analyzer, luceneFiled, fieldContent);
                    field.set(document, bestFragment);
                } else {
                    field.set(document, hitDoc.get(field.getName()));
                }
            }
            documentList.add(document);
            //如果不是搜索content域，那么需要为结果中的content格式化标签语言
            if (!luceneFiled.equalsIgnoreCase(EngineConfig.CONTENT.toLowerCase())) {
                String content = StringEscapeUtils.escapeHtml(hitDoc.get(EngineConfig.CONTENT));
                document.setContent(content);
            }
        }

        return documentList;
    }

}
