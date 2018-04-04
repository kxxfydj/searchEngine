package com.kxxfydj.service.impl;

import com.kxxfydj.enginer.SearchIndex;
import com.kxxfydj.form.HitDocument;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/4/4.
 */
public class SearchServiceImp implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImp.class);

    @Autowired
    private SearchIndex searchIndex;

    @Autowired
    RedisUtil<String,List<HitDocument>> redisUtil;

    @Override
    public List<HitDocument> defaultSearchContent(String content) {
        String clause = "content:" + content;
        Map<String, List<HitDocument>> listMap = redisUtil.hmget("documentList");
        List<HitDocument> documentList = listMap.get(clause);
        if(documentList != null || !documentList.isEmpty()){
            searchIndex.searchIndex(clause);
        }
        return null;
    }

    @Override
    public List<HitDocument> fieldSearch(String filed, String content) {
        return null;
    }
}
