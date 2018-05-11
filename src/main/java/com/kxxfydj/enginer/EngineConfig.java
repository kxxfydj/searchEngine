package com.kxxfydj.enginer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by kxxfydj on 2018/4/2.
 */
@Component
public class EngineConfig {
    @Value("#{settings['lucene.file.indexFile']}")
    private String indexFile;

    @Value("#{settings['lucene.file.docFile']}")
    private String documentFile;

    public static final  String FILENAME = "fileName";

    public static final String CONTENT = "content";

    public static final String PATH = "path";

    public static final String PROJECTNAME = "projectName";

    public static final String GITPATH = "gitPath";

    public static final String LANGUAGE = "language";

    public static final String REPOSITORY = "repository";

    @Value("#{settings['crawler.codefile.path']}")
    private String zipFilePath;

    @Value("#{settings['crawler.unzipfile.path']}")
    private String unzipFilePath;

    public String getZipFilePath() {
        return zipFilePath;
    }

    public void setZipFilePath(String zipFilePath) {
        this.zipFilePath = zipFilePath;
    }

    public String getUnzipFilePath() {
        return unzipFilePath;
    }

    public void setUnzipFilePath(String unzipFilePath) {
        this.unzipFilePath = unzipFilePath;
    }

    public String getIndexFile() {
        return indexFile;
    }

    public void setIndexFile(String indexFile) {
        this.indexFile = indexFile;
    }

    public String getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(String documentFile) {
        this.documentFile = documentFile;
    }
}
