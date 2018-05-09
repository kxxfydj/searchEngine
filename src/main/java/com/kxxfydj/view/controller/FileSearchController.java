package com.kxxfydj.view.controller;

import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.CodeContentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * Created by kxxfydj on 2018/5/7.
 */
@Controller
@RequestMapping("searchResource")
public class FileSearchController {

    private static final Logger logger = LoggerFactory.getLogger(FileSearchController.class);

    @Autowired
    private CodeContentService codeContentService;

    @Autowired
    private CrawlerConfig crawlerConfig;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "searchFile", method = RequestMethod.GET)
    public ModelAndView searchFile(String filePath) {
        if(StringUtils.isBlank(filePath)){
            logger.info("获取文件信息请求中文件的路径为空！");
            return null;
        }
        String wholePath;
        if(filePath.startsWith("\\")){
            wholePath = crawlerConfig.getCodeunzipPath() + filePath;
        }else {
            wholePath = crawlerConfig.getCodeunzipPath() + File.separator + filePath;
        }

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

    @RequestMapping(value = "directory_tree/{codeInfoId}/", method = RequestMethod.GET)
    @ResponseBody
    public String searchDir(@PathVariable int codeInfoId){
        List<String> fileList = codeContentService.getFileByCodeInfoId(codeInfoId);
        if(fileList == null || fileList.isEmpty()){
            return null;
        }

        CodeInfo codeInfo = redisUtil.get(RedisKeys.CODEINFOID.getKey() + ":" + codeInfoId);
        if(codeInfo == null){
            return null;
        }

        StringBuilder htmlString = new StringBuilder("{\"tree\": \"");
        String rootPath = crawlerConfig.getCodeunzipPath() + File.separator + codeInfo.getRepository() + File.separator + codeInfo.getProjectName();
        for(String filePath : fileList){
            String relativePath = filePath.substring(rootPath.length() + 1,filePath.length());
            htmlString.append("<a href=\\\"./searchFile.html?filePath=" + filePath.substring(crawlerConfig.getCodeunzipPath().length() + 1, filePath.length()).replaceAll("\\\\","\\\\\\\\") + "\\\">/" + relativePath.replaceAll("\\\\","\\\\\\\\") + "</a><br>");
        }
        htmlString.append("\"}");
//        StringBuilder htmlString = new StringBuilder("{\"tree\": \"<a href=\\\"/file/37250932/header.txt\\\">/header.txt</a>\"}");
        return htmlString.toString();
    }
}
