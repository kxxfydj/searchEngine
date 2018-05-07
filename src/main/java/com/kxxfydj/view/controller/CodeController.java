package com.kxxfydj.view.controller;

import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.form.HitDocument;
import com.kxxfydj.service.SearchService;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by kxxfydj on 2018/2/13.
 */
@Controller
@RequestMapping("/search")
public class CodeController {

    private static final Logger logger = LoggerFactory.getLogger(CodeController.class);

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "searchCode" ,method = RequestMethod.GET)
    public ModelAndView searchCode(String clause,int pageIndex){
        List<HitDocument> hitDocumentList = null;
        if(clause.contains(":")) {
            hitDocumentList = searchService.fieldSearch(clause);
        }else {
            hitDocumentList = searchService.defaultSearchContent(clause);
        }
        ModelAndView modelAndView = new ModelAndView("search/jsp/searchPage.jsp");
        int indexStart = (pageIndex - 1)*10;
        int indexEnd = (indexStart + 10) > hitDocumentList.size() ? hitDocumentList.size() : (indexStart + 10);
        int pageCount = hitDocumentList.size()/10;
        modelAndView.addObject("currentPage",pageIndex);
        modelAndView.addObject("pageCount",pageCount);
        modelAndView.addObject("hitList",hitDocumentList.subList(indexStart,indexEnd));

        MultiSet<String> languageCount = new HashMultiSet<>();
        MultiSet<String> repositoryCount = new HashMultiSet<>();
        for(HitDocument document : hitDocumentList){
            languageCount.add(document.getLanguage());
//            System.out.println(document.getPath().substring(document.getPath().indexOf("\\",document.getPath().indexOf("\\")+1),document.getPath().length()));
            repositoryCount.add(document.getRepository());
        }
        modelAndView.addObject("languageCount",languageCount);
        modelAndView.addObject("repositoryCount",repositoryCount);

        modelAndView.addObject("searchClause",clause);
        return modelAndView;
    }
}
