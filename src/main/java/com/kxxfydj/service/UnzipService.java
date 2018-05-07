package com.kxxfydj.service;

import com.kxxfydj.entity.CrawlerTask;

/**
 * Created by kxxfydj on 2018/4/28.
 */
public interface UnzipService {
    void fileToDatabase(String repository, String filePath, boolean isUpdate);
}
