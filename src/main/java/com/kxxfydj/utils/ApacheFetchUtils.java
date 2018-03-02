package com.kxxfydj.utils;

import com.google.common.collect.Table;
import com.kxxfydj.webmagicext.MyHttpClientDownloader;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class ApacheFetchUtils {
	private ApacheFetchUtils() {
	}

    /**
     * http get 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param milliseconds 超时毫秒数
     * @return html字符数组
     * @throws IOException IO异常
     */
    public static byte[] getBytes(ApacheHttpRequestData data, String url, int milliseconds) throws IOException {
        Site site = data.getSite();
        site.setTimeOut(milliseconds);

        Request request = new Request();
        request.setMethod(HttpConstant.Method.GET);
        request.setUrl(url);
        request.putExtra(MyHttpClientDownloader.EXTRA_IS_AUTO_REDIRECT, data.isAutoRedirect());

        return getBytesFromPage(data, request);
    }

    /**
     * http get 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @return html字符数组
     * @throws IOException IO异常
     */
    public static byte[] getBytes(ApacheHttpRequestData data, String url, Map<String, String> inputs) throws IOException {
        String completeUrl = appendParameterToUrl(url, inputs);
        return getBytes(data, completeUrl, data.getSite().getTimeOut());
    }

    /**
     * http get 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @return html字符数组
     * @throws IOException IO异常
     */
    public static byte[] getBytes(ApacheHttpRequestData data, String url) throws IOException {
		return getBytes(data, url, data.getSite().getTimeOut());
	}

    /**
     * http post 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param milliseconds 超时毫秒数
     * @param  headers 请求头信息
     * @param encode 参数编码类型
     * @return html字符数组
     * @throws IOException IO异常
     */
    public static byte[] postBytes(ApacheHttpRequestData data, String url, Map<String, String> inputs, int milliseconds, Map<String, String> headers, String encode) throws IOException {
        Site site = data.getSite();
        site.setTimeOut(milliseconds);
        addHeadersToSite(headers, site);

        Request request = new Request();
        request.setMethod(HttpConstant.Method.POST);
        request.setUrl(url);
        Map<String, Object> extras = inputsToExtras(inputs, data);
        if(StringUtils.isNotEmpty(encode)) {
            extras.put(MyHttpClientDownloader.EXTRA_CHARSET, encode);
        }
        request.setExtras(extras);
        request.putExtra(MyHttpClientDownloader.EXTRA_IS_AUTO_REDIRECT, data.isAutoRedirect());

        return getBytesFromPage(data, request);
    }

    /**
     * http post 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @return html字符数组
     * @throws IOException IO异常
     */
	public static byte[] postBytes(ApacheHttpRequestData data, String url, Map<String, String> inputs) throws IOException {
		return postBytes(data, url, inputs, data.getSite().getTimeOut());
	}

    /**
     * http post 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param headers 请求头信息
     * @return html字符数组
     * @throws IOException IO异常
     */
	public static byte[] postBytes(ApacheHttpRequestData data, String url, Map<String, String> inputs, Map<String, String> headers) throws IOException {
		return postBytes(data, url, inputs, data.getSite().getTimeOut(), headers);
	}

    /**
     * http post 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param milliseconds 超时毫秒数
     * @return html字符数组
     * @throws IOException IO异常
     */
	public static byte[] postBytes(ApacheHttpRequestData data, String url, Map<String, String> inputs, int milliseconds) throws IOException {
		if (data.isKeepHeaders())
			return postBytes(data, url, inputs, milliseconds, data.getHeaders());
		else
			return postBytes(data, url, inputs, milliseconds, null);
	}

    /**
     * http post 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param milliseconds 超时毫秒数
     * @param  headers 请求头信息
     * @return html字符数组
     * @throws IOException IO异常
     */
    public static byte[] postBytes(ApacheHttpRequestData data, String url, Map<String, String> inputs, int milliseconds, Map<String, String> headers) throws IOException {
        return postBytes(data, url, inputs, milliseconds, headers, null);
    }

    /**
     * http post 字符数组
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param encode 参数编码类型
     * @return html字符数组
     * @throws IOException IO异常
     */
    public static byte[] postBytes(ApacheHttpRequestData data, String url, Map<String, String> inputs, String encode) throws IOException {
        return postBytes(data, url, inputs, data.getSite().getTimeOut(), null, encode);
    }

    /**
     * http get html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param milliseconds 超时毫秒数
     * @param charset 编码字符集
     * @return html字符串
     * @throws IOException IO异常
     */
    public static String get(ApacheHttpRequestData data, String url, String charset, int milliseconds) throws IOException {
        String tempUrl = url;
        if (url.contains("|")) {
            tempUrl = url.replace("|", URLEncoder.encode("|", MyHttpClientDownloader.DEFAULT_CHARSET));
        }
        Site site = data.getSite();
        site.setTimeOut(milliseconds);

        Request request = new Request();
        request.setMethod(HttpConstant.Method.GET);
        request.setUrl(tempUrl);
        Map<String, Object> extras = new HashMap<>();
        if(StringUtils.isNotEmpty(charset)) {
            extras.put(MyHttpClientDownloader.EXTRA_CHARSET, charset);
        }
        request.setExtras(extras);
        request.putExtra(MyHttpClientDownloader.EXTRA_IS_AUTO_REDIRECT, data.isAutoRedirect());

        return getHtmlFromPage(data, request);
    }

    /**
     * http get html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param charset 编码字符集
     * @param milliseconds 超时毫秒数
     * @return html字符串
     * @throws IOException IO异常
     */
    public static String get(ApacheHttpRequestData data, String url, Map<String, String> inputs, String charset, int milliseconds) throws IOException {
        String completeUrl = appendParameterToUrl(url, inputs);
        return get(data, completeUrl, charset, milliseconds);
    }

    /**
     * http get html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String get(ApacheHttpRequestData data, String url) throws IOException {
		return get(data, url, MyHttpClientDownloader.DEFAULT_CHARSET, data.getSite().getTimeOut());
	}

    /**
     * http get html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param charset 编码字符集
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String get(ApacheHttpRequestData data, String url, String charset) throws IOException {
		return get(data, url, charset, data.getSite().getTimeOut());
	}

    /**
     * http get html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param timeout 超时毫秒数
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String get(ApacheHttpRequestData data, String url, int timeout) throws IOException {
		return get(data, url, MyHttpClientDownloader.DEFAULT_CHARSET, timeout);
	}

    /**
     * http get html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param charset 编码字符集
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String get(ApacheHttpRequestData data, String url, Map<String, String> input, String charset) throws IOException {
		return get(data,url,input,charset,data.getSite().getTimeOut());
	}

    /**
     * http post html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param headers 请求头信息
     * @param charset 编码字符集
     * @param milliseconds 超时毫秒数
     * @return html字符串
     * @throws IOException IO异常
     */
    public static String post(ApacheHttpRequestData data, String url, Map<String, String> inputs, Map<String, String> headers, String charset, int milliseconds) throws IOException {
        Site site = data.getSite();
        site.setTimeOut(milliseconds);
        addHeadersToSite(headers, site);

        Request request = new Request();
        request.setMethod(HttpConstant.Method.POST);
        request.setUrl(url);
        Map<String, Object> extras = inputsToExtras(inputs, data);
        if(StringUtils.isNotEmpty(charset)) {
            extras.put(MyHttpClientDownloader.EXTRA_CHARSET, charset);
        }
        request.setExtras(extras);
        request.putExtra(MyHttpClientDownloader.EXTRA_IS_AUTO_REDIRECT, data.isAutoRedirect());

        return getHtmlFromPage(data, request);
    }

    /**
     * http post html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @return html字符串
     * @throws IOException IO异常
     */
    public static String post(ApacheHttpRequestData data, String url) throws IOException {
        return post(data, url, null);
    }

    /**
     * http post html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param milliseconds 超时毫秒数
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String post(ApacheHttpRequestData data, String url, Map<String, String> inputs, int milliseconds) throws IOException {
		if (data.isKeepHeaders())
			return post(data, url, inputs, data.getHeaders(), MyHttpClientDownloader.DEFAULT_CHARSET, milliseconds);
		else
			return post(data, url, inputs, null, MyHttpClientDownloader.DEFAULT_CHARSET, milliseconds);
	}

    /**
     * http post html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String post(ApacheHttpRequestData data, String url, Map<String, String> inputs) throws IOException {
		if (data.isKeepHeaders())
			return post(data, url, inputs, data.getHeaders(), MyHttpClientDownloader.DEFAULT_CHARSET, data.getSite().getTimeOut());
		else
			return post(data, url, inputs, null, MyHttpClientDownloader.DEFAULT_CHARSET, data.getSite().getTimeOut());
	}

    /**
     * http post html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param charset 编码字符集
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String post(ApacheHttpRequestData data, String url, Map<String, String> inputs, String charset) throws IOException {
		if (data.isKeepHeaders())
			return post(data, url, inputs, data.getHeaders(), charset, data.getSite().getTimeOut());
		else
			return post(data, url, inputs, null, charset, data.getSite().getTimeOut());
	}

    /**
     * http post html
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param inputs 请求参数
     * @param headers 请求头信息
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String post(ApacheHttpRequestData data, String url, Map<String, String> inputs, Map<String, String> headers) throws IOException {
		return post(data, url, inputs, headers, MyHttpClientDownloader.DEFAULT_CHARSET, data.getSite().getTimeOut());
	}

    /**
     * http post data
     * 即不是post key-value模型的数据，而是post一段数据
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param param 请求参数数据
     * @param headers 请求头信息
     * @param charset 编码字符集
     * @return html字符串
     * @throws IOException IO异常
     */
    public static String postData(ApacheHttpRequestData data, String url, String param, String charset, Map<String, String> headers) throws IOException {
        Site site = data.getSite();
        addHeadersToSite(headers, site);

        Request request = new Request();
        request.setMethod(HttpConstant.Method.POST);
        request.setUrl(url);
        Map<String, String> inputs = new HashMap<>();
        Map<String, Object> extras = inputsToExtras(inputs, data);
        if(StringUtils.isNotEmpty(charset)) {
            extras.put(MyHttpClientDownloader.EXTRA_CHARSET, charset);
        }
        if (headers!= null && headers.get(HTTP.CONTENT_TYPE) != null) {
            extras.put(HTTP.CONTENT_TYPE, headers.get(HTTP.CONTENT_TYPE));
        }
        request.setExtras(extras);
        request.putExtra("data", param);
        request.putExtra(MyHttpClientDownloader.EXTRA_IS_AUTO_REDIRECT, data.isAutoRedirect());

        return getHtmlFromPage(data, request);
    }

    /**
     * http post data
     * 即不是post key-value模型的数据，而是post一段数据
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param param 请求参数数据
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String postData(ApacheHttpRequestData data, String url, String param) throws IOException {
		return postData(data, url, param, MyHttpClientDownloader.DEFAULT_CHARSET);
	}

    /**
     * http post data
     * 即不是post key-value模型的数据，而是post一段数据
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param param 请求参数数据
     * @param charset 编码字符集
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String postData(ApacheHttpRequestData data, String url, String param, String charset) throws IOException {
		if (data.isKeepHeaders())
			return postData(data, url, param, charset, data.getHeaders());
		else
			return postData(data, url, param, charset, null);
	}

    /**
     * http post data
     * 即不是post key-value模型的数据，而是post一段数据
     * @param data ApacheHttpRequestData
     * @param url 请求地址
     * @param param 请求参数数据
     * @param headers 请求头信息
     * @return html字符串
     * @throws IOException IO异常
     */
	public static String postData(ApacheHttpRequestData data, String url, String param, Map<String, String> headers) throws IOException {
		return postData(data, url, param, MyHttpClientDownloader.DEFAULT_CHARSET, headers);
	}

	private static Map<String, Object> inputsToExtras(Map<String, String> inputs, ApacheHttpRequestData data){
		Map<String, Object> extras = new HashMap<>();
		extras.putAll(data.getRequestExtras());
		if(inputs != null && !inputs.isEmpty()) {
			NameValuePair[] nameValuePairs = new NameValuePair[inputs.size()];
			int idx = 0;
			for (Entry<String, String> entry : inputs.entrySet()) {
				NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
				nameValuePairs[idx++] = nameValuePair;
			}
			extras.put("nameValuePair", nameValuePairs);
		}
		return extras;
	}

	private static void addHeadersToSite(Map<String, String> headers, Site site){
		if(headers != null) {
			site.getHeaders().putAll(headers);
		}
	}

	private static String getHtmlFromPage(ApacheHttpRequestData data, Request request){
		Site site = data.getSite();
        Task task = site.toTask();
        MyHttpClientDownloader httpClient = data.getHttpClient();
        CloseableHttpResponse httpResponse = executeDowland(data, request, task);
        Page page = httpClient.getPageFromResponse(httpResponse, request, task);
        if(page != null){
            return page.getRawText();
        }
        return null;
	}

    private static byte[] getBytesFromPage(ApacheHttpRequestData data, Request request){
        Site site = data.getSite();
        Task task = site.toTask();
        MyHttpClientDownloader httpClient = data.getHttpClient();
        CloseableHttpResponse httpResponse = executeDowland(data, request, task);
        return httpClient.getBytesFromResponse(httpResponse, request, task);
    }

    private static CloseableHttpResponse executeDowland(ApacheHttpRequestData data, Request request, Task task){
        MyHttpClientDownloader httpClient = data.getHttpClient();
        //追加cookies，如果有
        appendCookies(data, request);
        CloseableHttpResponse httpResponse = httpClient.downloadResponse(request, task);

        //设置响应状态码
        data.setStatusCode((Integer) request.getExtra(Request.STATUS_CODE));
        /*
         * 问题：headers数组中可能会存在重复的key，eg：Set-Cookie
         *      把headers数组转存到responseHeaders map集合，重复的key会被覆盖
         *
         * 方案：定义repeatNameIndex集合，记录重复的key，如果存在重复的key
         *       使用(key + index)的方式改造key，再存入responseHeaders集合
         *
         * 注：为了兼容，原始的重复key也会保留
         */
        if(data.isGetResponseHeaders()){
            Header[] headers = httpResponse.getAllHeaders();
            Map<String, Integer> repeatNameIndex = new HashMap<>();
            for (Header header : headers){
                String name = header.getName();
                // 记录key出现次数
                if(repeatNameIndex.containsKey(name)){
                    repeatNameIndex.put(name, (repeatNameIndex.get(name)) + 1);
                }else {
                    repeatNameIndex.put(name, 1);
                }

                data.addResponseHeaders(name, header.getValue());

                Map<String, String> responseHeaders = data.getResponseHeaders();
                if (responseHeaders.containsKey(name)){
                    data.addResponseHeaders(name + repeatNameIndex.get(name), header.getValue());
                }
            }
            repeatNameIndex.clear();
        }
        return httpResponse;
    }

    private static void appendCookies(ApacheHttpRequestData data, Request request) {
        Table<String, String, String> appendCookies = data.getAppendCookies();
        if(appendCookies != null && !appendCookies.isEmpty()) {
            request.putExtra(MyHttpClientDownloader.EXTRA_APPEND_COOKIES, appendCookies.rowMap());
        }
    }

    private static String appendParameterToUrl(String url, Map<String, String> inputs) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(url);
        boolean first = true;
        if (!url.contains("?")) {
            urlBuilder.append("?");
        }
        for (Entry<String, String> entry : inputs.entrySet()) {
            if (!first) {
                urlBuilder.append('&');
            }else {
                first = false;
            }
            urlBuilder.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), MyHttpClientDownloader.DEFAULT_CHARSET));
        }
        return urlBuilder.toString();
    }
}
