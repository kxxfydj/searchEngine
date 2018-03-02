package com.kxxfydj.utils;

import com.kxxfydj.common.CommonTag;
import org.apache.http.NameValuePair;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class RequestUtil {

    private RequestUtil(){

    }

    public static Request createGetRequest(String url,String type){
        return createRequest(url,HttpConstant.Method.GET,type,null);
    }

    public static Request createPostRequest(String url,String type,NameValuePair[] nameValuePairs){
        return createRequest(url, HttpConstant.Method.POST,type,nameValuePairs);
    }

    private static Request createRequest(String url, String method,String type, NameValuePair[] nameValuePairs){
        Request request = new Request(url);
        request.setMethod(method);
        request.putExtra(CommonTag.TYPE,type);

        if(nameValuePairs != null) {
            Map<String, Object> form = new HashMap<>();
            form.put("nameValuePair", nameValuePairs);
            request.setExtras(form);
        }
        return request;
    }
}
