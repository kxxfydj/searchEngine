package com.kxxfydj.utils;

import org.apache.commons.lang.StringUtils;

import java.text.NumberFormat;

/**
 * Created by kxxfydj on 2018/3/14.
 */
public class NumberFormatUtil {
    public static int formatInt(String intString){
        if(StringUtils.isBlank(intString)){
            throw new RuntimeException("the number String is null");
        }

        String noComma = intString.replaceAll(",","").replaceAll("k","000");
        return Integer.parseInt(noComma);
    }

}
