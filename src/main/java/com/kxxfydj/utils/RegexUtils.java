package com.kxxfydj.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kxxfydj on 2017/10/6.
 */
public class RegexUtils {
    public static String singleExtract(String content,String regex,int group){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }
}
