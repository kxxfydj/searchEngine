package com.kxxfydj.service.impl;

import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.enginer.SearchIndex;
import com.kxxfydj.form.HitDocument;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.util.List;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/4/4.
 */
@Service
public class SearchServiceImp implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImp.class);

    @Autowired
    private SearchIndex searchIndex;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<HitDocument> defaultSearchContent(String content) {
        String clause = "content:" + content;
        return handlerSearch(clause);
    }

    @Override
    public List<HitDocument> fieldSearch(String clause) {
        return handlerSearch(clause);
    }

    private List<HitDocument> handlerSearch(String clause){
        List<HitDocument> documentList = redisUtil.lGet(RedisKeys.DOCUMENTLIST.getKey() + ":" + clause,0,-1);
        if( documentList == null || documentList.isEmpty()){
            return searchIndex.searchIndex(clause);
        }
        logger.info("查询语句有缓存，从缓存中读取数据");
        return documentList;
    }
}
