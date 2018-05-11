package com.kxxfydj.enginer;

import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.redis.RedisUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kxxfydj on 2018/4/2.
 */
public class IndexManager {
    private static final Logger logger = LoggerFactory.getLogger(IndexManager.class);

    private EngineConfig engineConfig;

    private Analyzer analyzer;

    private IndexWriterConfig indexWriterConfig;

    public IndexManager(EngineConfig engineConfig) {
        analyzer = new StandardAnalyzer();
        indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        this.engineConfig = engineConfig;
    }

    public void createIndex(List<CodeContent> codeContentList, RedisUtil redisUtil) {

        Path indexFile = Paths.get(engineConfig.getIndexFile());

        if (!indexFile.toFile().exists()) {
            indexFile.toFile().mkdirs();
        }

        long startTime = System.currentTimeMillis();

        try (
                FSDirectory fsDirectory = FSDirectory.open(indexFile);
                IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig)
        ) {
            for (CodeContent codeContent : codeContentList) {
                Document document = new Document();
                String[] paths = codeContent.getPath().split("\\\\");
                String first = "";
                String[] others = null;
                if (paths.length == 1) {
                    first = paths[0];
                } else if (paths.length > 1) {
                    first = paths[0];
                    others = Arrays.copyOfRange(paths, 1, paths.length);
                }
                int startindex = engineConfig.getUnzipFilePath().split("\\\\").length - 1;
                Path filePath = Paths.get(first, others);
                CodeInfo codeInfo = redisUtil.get(RedisKeys.CODEINFOID.getKey() + ":" + codeContent.getCodeInfoId());
                document.add(new StringField(EngineConfig.FILENAME, filePath.getFileName().toString(), Field.Store.YES));
                document.add(new StringField(EngineConfig.PATH, filePath.subpath(startindex, filePath.getNameCount()).toString(), Field.Store.YES));
                document.add(new TextField(EngineConfig.CONTENT, codeContent.getBody(), Field.Store.YES));
                document.add(new StringField(EngineConfig.PROJECTNAME, codeInfo.getProjectName(),Field.Store.YES));
                document.add(new StringField(EngineConfig.GITPATH,codeInfo.getGitPath(),Field.Store.YES));
                document.add(new StringField(EngineConfig.LANGUAGE,codeContent.getLanguage(),Field.Store.YES));
                document.add(new StringField(EngineConfig.REPOSITORY,codeInfo.getRepository(),Field.Store.YES));

                indexWriter.addDocument(document);

            }
            long commits = indexWriter.commit();
            long endTime = System.currentTimeMillis();
            logger.info("成功生成索引{}条，共耗时{}毫秒", commits, endTime - startTime);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


    }
}
