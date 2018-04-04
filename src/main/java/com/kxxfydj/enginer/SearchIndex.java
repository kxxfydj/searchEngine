package com.kxxfydj.enginer;

import com.kxxfydj.form.HitDocument;
import com.kxxfydj.redis.RedisUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private RedisUtil<String,HitDocument> redisUtil;

    @Autowired
    private EngineConfig engineConfig;

    public SearchIndex(EngineConfig engineConfig){
        this.engineConfig = engineConfig;
    }

    public List<HitDocument> searchIndex(String clauses){
        try(
                Directory directory = FSDirectory.open(Paths.get(engineConfig.getIndexFile()));
                Analyzer analyzer = new StandardAnalyzer();
                DirectoryReader reader = DirectoryReader.open(directory)
        ){
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            QueryParser queryParser = new QueryParser(EngineConfig.CONTENT,analyzer);
            Query query = queryParser.parse(clauses);
            ScoreDoc[] hits = indexSearcher.search(query,1000).scoreDocs;

            List<HitDocument> documentList = new ArrayList<>();
            for(ScoreDoc doc : hits){
                HitDocument document = new HitDocument();
                Document hitDoc = indexSearcher.doc(doc.doc);

                document.setFileName(hitDoc.get(EngineConfig.FILENAME));
                document.setContent(hitDoc.get(EngineConfig.CONTENT));
                document.setPath(hitDoc.get(EngineConfig.PATH));

                documentList.add(document);
            }

            redisUtil.lSet("documentList:" + clauses,documentList,60*60);   //一小时过期
            logger.info("lucene查询字段结果已缓存 clause:{}  并设置一小时过期", clauses);

            return documentList;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
