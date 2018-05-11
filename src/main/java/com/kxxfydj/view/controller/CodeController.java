package com.kxxfydj.view.controller;

import com.kxxfydj.form.HitDocument;
import com.kxxfydj.form.SearchParam;
import com.kxxfydj.service.SearchService;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by kxxfydj on 2018/2/13.
 */
@Controller
@RequestMapping("/search")
public class CodeController {

    private static final Logger logger = LoggerFactory.getLogger(CodeController.class);

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "searchCode", method = RequestMethod.GET)
    public ModelAndView searchCode(String clause, int pageIndex) {
        List<HitDocument> hitDocumentList = getDocumentList(clause);
        ModelAndView modelAndView = new ModelAndView("search/jsp/searchPage.jsp");
        int indexStart = (pageIndex - 1) * 10;
        int indexEnd = (indexStart + 10) > hitDocumentList.size() ? hitDocumentList.size() : (indexStart + 10);
        int pageCount = hitDocumentList.size() / 10;
        modelAndView.addObject("currentPage", pageIndex);
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("hitList", hitDocumentList.subList(indexStart, indexEnd));
        modelAndView.addObject("totalSize", hitDocumentList.size());

        MultiSet<String> languageCount = new HashMultiSet<>();
        MultiSet<String> repositoryCount = new HashMultiSet<>();
        for (HitDocument document : hitDocumentList) {
            languageCount.add(document.getLanguage());
            repositoryCount.add(document.getRepository());
        }
        modelAndView.addObject("languageCount", languageCount);
        modelAndView.addObject("repositoryCount", repositoryCount);

        modelAndView.addObject("searchClause", clause);
        return modelAndView;
    }

    @RequestMapping(value = "searchCodeFilter", method = RequestMethod.POST)
    public ModelAndView searchCodeFilter(SearchParam formParam) {
        List<HitDocument> OriginHitDocumentList = getDocumentList(formParam.getClause());

        List<HitDocument> hitDocumentList = OriginHitDocumentList.stream().filter(document -> {
            if (StringUtils.isNotBlank(formParam.getLanguage()) && StringUtils.isNotBlank(formParam.getRepository())) {
                return Objects.equals(document.getLanguage().toLowerCase(), formParam.getLanguage().toLowerCase())
                        && Objects.equals(document.getRepository().toLowerCase(), formParam.getRepository().toLowerCase());
            }else if(StringUtils.isBlank(formParam.getLanguage()) || StringUtils.isNotBlank(formParam.getRepository())){
                return Objects.equals(document.getRepository().toLowerCase(), formParam.getRepository().toLowerCase());
            }else if(StringUtils.isNotBlank(formParam.getLanguage()) || StringUtils.isBlank(formParam.getRepository())){
                return Objects.equals(document.getLanguage().toLowerCase(), formParam.getLanguage().toLowerCase());
            }else {
                return true;
            }
        }).collect(Collectors.toList());

        ModelAndView modelAndView = new ModelAndView("search/jsp/searchPage.jsp");
        int indexStart = (formParam.getPageIndex() - 1) * 10;
        int indexEnd = (indexStart + 10) > hitDocumentList.size() ? hitDocumentList.size() : (indexStart + 10);
        int pageCount = hitDocumentList.size() / 10;

        modelAndView.addObject("currentPage", formParam.getPageIndex());
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("hitList", hitDocumentList.subList(indexStart, indexEnd));
        modelAndView.addObject("totalSize", hitDocumentList.size());

        MultiSet<String> languageCount = new HashMultiSet<>();
        MultiSet<String> repositoryCount = new HashMultiSet<>();
        for (HitDocument document : hitDocumentList) {
            languageCount.add(document.getLanguage());
            repositoryCount.add(document.getRepository());
        }
        modelAndView.addObject("languageCount", languageCount);
        modelAndView.addObject("repositoryCount", repositoryCount);

        modelAndView.addObject("searchClause", formParam.getClause());
        return modelAndView;
    }

    private List<HitDocument> getDocumentList(String clause) {
        List<HitDocument> hitDocumentList = null;
        if (clause.contains(":")) {
            hitDocumentList = searchService.fieldSearch(clause);
        } else {
            hitDocumentList = searchService.defaultSearchContent(clause);
        }
        return hitDocumentList;
    }

}
