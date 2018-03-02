package com.kxxfydj.utils;

import java.util.HashMap;
import java.util.Map;

public class HeaderUtils {
	public static final String ACCEPT = "Accept";
	public static final String ACCEPTENCODING = "Accept-Encoding";
	public static final String ACCEPTLANGUAGE = "Accept-Language";
	public static final String CONNECTION = "Connection";
	public static final String COOKIE = "Cookie";
	public static final String HOST = "Host";
	public static final String REFERER = "Referer";
	public static final String USERAGENT = "User-Agent";
	public static final String CACHECONTROL = "Cache-Control";
	public static final String CONTENTTYPE = "Content-Type";
	public static final String ORIGIN = "Origin";
	public static final String SETCOOKIE = "Set-Cookie";
	public static final String DNT = "DNT";
	public static final String XREQUESTEDWITH = "X-Requested-With";
	public static final String UPGRADEINSECUREREQUESTS = "Upgrade-Insecure-Requests";
	
	
	private HeaderUtils() {
	}
	
	public static Map<String, String> initPostHeaders(String host, String referer, String contentType, String userAgent) {
		Map<String, String> headers = new HashMap<>();
		headers.put(ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put(ACCEPTENCODING, "gzip, deflate, sdch");
		headers.put(ACCEPTLANGUAGE, "zh-CN,zh;q=0.8");
		headers.put(CONNECTION, "Keep-Alive");
		headers.put(HOST, host);
		headers.put(REFERER, referer);
		headers.put(USERAGENT, userAgent);
		headers.put(CACHECONTROL, "no-cache");
		headers.put(CONTENTTYPE, contentType);
		return headers;
	}

	public static Map<String, String> initGetHeaders(String host, String referer, String userAgent) {
		Map<String, String> headers = new HashMap<>();
		headers.put(ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put(ACCEPTENCODING, "gzip, deflate, sdch");
		headers.put(ACCEPTLANGUAGE, "zh-CN,zh;q=0.8");
		headers.put(CONNECTION, "keep-alive");
		headers.put(HOST, host);
		headers.put(REFERER, referer);
		headers.put(USERAGENT, userAgent);
		return headers;
	}

	public static String getHost(String targetUrl) {
		String regex = "http[s]*://([\\.\\w]+)/.*";
		return RegexUtil.singleExtract(targetUrl, regex, 1);
	}

	
	
}
