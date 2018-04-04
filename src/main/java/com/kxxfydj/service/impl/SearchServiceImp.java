package com.kxxfydj.service.impl;

import com.kxxfydj.form.HitDocument;
import com.kxxfydj.service.SearchService;

import java.util.List;

/**
 * Created by kxxfydj on 2018/4/4.
 */
public class SearchServiceImp implements SearchService {
    @Override
    public List<HitDocument> defaultSearchContent(String content) {
        return null;
    }

    @Override
    public List<HitDocument> fieldSearch(String filed, String content) {
        return null;
    }
}
