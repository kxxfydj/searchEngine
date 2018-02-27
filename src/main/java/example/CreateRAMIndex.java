package example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.sql.Connection;

/**
 * Created by kxxfydj on 2018/1/26.
 */
public class CreateRAMIndex {
    RAMDirectory dir = new RAMDirectory();

    public static IndexReader indexReader;
    private Connection conn;
    public void createIndex() {
        try {
            System.out.println("========试试水=====");
            Analyzer analyzer = new StandardAnalyzer(); //分词器（有简体中文分词器）
            IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(analyzer));
            Document doc = new Document();
            Document doc1 = new Document();
            Document doc2 = new Document();
            Document doc3 = new Document();
            FieldType fieldType = new FieldType();
            fieldType.setStored(true); // 设置为true，存储此字段
            doc.add(new Field("bookName", "白夜行", fieldType));
            doc1.add(new Field("bookName", "时间移民", fieldType));
            doc2.add(new Field("bookName", "假面饭店", fieldType));
            doc3.add(new Field("bookName", "废都", fieldType));
            indexWriter.addDocument(doc);
            indexWriter.addDocument(doc1);
            indexWriter.addDocument(doc2);
            indexWriter.addDocument(doc3);
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        CreateRAMIndex s = new CreateRAMIndex();
        s.createIndex();
    }
}
