package com.kxxfydj.service;

import com.kxxfydj.form.HitDocument;

import java.util.List;

/**
 * Created by kxxfydj on 2018/4/4.
 */
public interface SearchService {
    List<HitDocument> defaultSearchContent(String content);
    List<HitDocument> fieldSearch(String filed,String content);
}
