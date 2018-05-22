package com.kxxfydj.controller;

import com.kxxfydj.common.RedisKeys;
import com.kxxfydj.crawlerConfig.CrawlerConfig;
import com.kxxfydj.entity.CodeContent;
import com.kxxfydj.entity.CodeInfo;
import com.kxxfydj.redis.RedisUtil;
import com.kxxfydj.service.CodeContentService;
import com.kxxfydj.service.CodeInfoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kxxfydj on 2018/5/7.
 */
@Controller
@RequestMapping("searchResource")
public class FileSearchController {

    private static final Logger logger = LoggerFactory.getLogger(FileSearchController.class);

    @Autowired
    private CodeInfoService codeInfoService;

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
        if(filePath.startsWith(File.separator)){
            wholePath = crawlerConfig.getCodePath() + filePath;
        }else {
            wholePath = crawlerConfig.getCodePath() + File.separator + filePath;
        }

        CodeContent codeContent = codeContentService.getFile(wholePath);
        if(codeContent == null){
            return null;
        }
        CodeInfo codeInfo = redisUtil.get(RedisKeys.CODEINFOID.getKey() + ":" + codeContent.getCodeInfoId());
        if(codeInfo == null){
            List<CodeInfo> codeInfoList = codeInfoService.getAllCodeInfo();
            //更新redis缓存
            List<String> codeInfoKeys = new ArrayList<>();
            for (CodeInfo ci : codeInfoList) {
                codeInfoKeys.add(RedisKeys.CODEINFOID.getKey() + ":" + ci.getId());
            }
            redisUtil.setForBatch(codeInfoKeys,codeInfoList);
            codeInfo = redisUtil.get(RedisKeys.CODEINFOID.getKey() + ":" + codeContent.getCodeInfoId());
            if(codeInfo == null){
                return null;
            }
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
        String rootPath = crawlerConfig.getCodePath() + File.separator + codeInfo.getRepository() + File.separator + codeInfo.getProjectName();
        for(String filePath : fileList){
            String relativePath = filePath.substring(rootPath.length() + 1,filePath.length());
            htmlString.append("<a href=\\\"./searchFile.html?filePath=" +
                    URLEncoder.encode(filePath.substring(crawlerConfig.getCodePath().length() + 1, filePath.length()).replaceAll(Pattern.quote(File.separator),Matcher.quoteReplacement(File.separator))) + "\\\">/" +
                    relativePath.replaceAll(Pattern.quote(File.separator), Matcher.quoteReplacement(File.separator) + Matcher.quoteReplacement(File.separator)) +
                    "</a><br>");
        }
        htmlString.append("\"}");
        return htmlString.toString();
    }
}
