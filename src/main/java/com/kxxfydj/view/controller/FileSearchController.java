package com.kxxfydj.view.controller;

import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.CodeContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;

/**
 * Created by kxxfydj on 2018/5/7.
 */
@Controller
@RequestMapping("searchResource")
public class FileSearchController {

    @Autowired
    private CodeContentService codeContentService;

    @Autowired
    private CrawlerConfig crawlerConfig;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "searchFile", method = RequestMethod.GET)
    public ModelAndView searchFile(String filePath) {
        String wholePath = crawlerConfig.getCodeunzipPath() + File.separator + filePath;
//        wholePath = wholePath.replaceAll("\\\\","\\\\\\\\");

        CodeContent codeContent = codeContentService.getFile(wholePath);
        if(codeContent == null){
            return null;
        }
        CodeInfo codeInfo = redisUtil.get(RedisKeys.CODEINFOID.getKey() + ":" + codeContent.getCodeInfoId());
        if(codeInfo == null){
            return null;
        }
        ModelAndView modelAndView = new ModelAndView("search/jsp/searchFile.jsp");
        modelAndView.addObject("codeContent", codeContent);
        modelAndView.addObject("codeInfo",codeInfo);
        return modelAndView;
    }

    @RequestMapping(value = "searchDir", method = RequestMethod.GET)
    public ModelAndView searchDir(String filePath){
        String wholePath = crawlerConfig.getCodeunzipPath() + File.separator + filePath;
        List<CodeContent> codeContentList = codeContentService.getFileChildren(wholePath);
        if(codeContentList == null || codeContentList.isEmpty()){
            return null;
        }

        CodeInfo codeInfo = redisUtil.get(RedisKeys.CODEINFOID.getKey() + ":" + codeContentList.get(0).getCodeInfoId());
        if(codeInfo == null){
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("search/jsp/searchDirectory.jsp");
        modelAndView.addObject("codeContentList",codeContentList);
        modelAndView.addObject("codeInfo",codeInfo);
        modelAndView.addObject("currentPath",filePath);
        return modelAndView;
    }
}
