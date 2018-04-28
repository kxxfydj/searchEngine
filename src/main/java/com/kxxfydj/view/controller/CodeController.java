package com.kxxfydj.view.controller;

import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.form.HitDocument;
import com.kxxfydj.service.SearchService;
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
    public ModelAndView searchCode(String clause){
        List<HitDocument> hitDocumentList = null;
        if(clause.contains(":")) {
            hitDocumentList = searchService.fieldSearch(clause);
        }else {
            hitDocumentList = searchService.defaultSearchContent(clause);
        }
        ModelAndView modelAndView = new ModelAndView("search/jsp/searchPage.jsp");
        modelAndView.addObject("hitList",hitDocumentList);
        return modelAndView;
    }
}
