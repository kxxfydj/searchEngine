package com.kxxfydj.utils;

import com.kxxfydj.exception.ResponseStatusException;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.List;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/3/5.
 */
public class HttpsUtils {

    public static final String POST = "post";

    public static final String GET = "get";

    private static final Logger logger = LoggerFactory.getLogger(HttpsUtils.class);

    public static String get(String url, JsoupRequestData jsoupRequestData, Map<String, String> cookies) {
        return get(url, jsoupRequestData, cookies, JsoupRequestData.getDefaultTimeout());
    }

    public static Document getDocument(String url, JsoupRequestData jsoupRequestData, Map<String, String> cookies) {
        Connection connection = constructConnection(url, jsoupRequestData, cookies, JsoupRequestData.getDefaultTimeout(), HttpsUtils.GET);
        return getDocumentFromRequest(connection, jsoupRequestData);
    }

    public static byte[] getBytes(String url, JsoupRequestData jsoupRequestData, Map<String, String> cookies) {
        return getBytes(url, jsoupRequestData, cookies, JsoupRequestData.getDefaultTimeout());
    }

    public static Document post(String url, JsoupRequestData jsoupRequestData, Map<String, String> cookies) {
        Connection connection = constructConnection(url, jsoupRequestData, cookies, JsoupRequestData.getDefaultTimeout(), HttpsUtils.POST);
        return getDocumentFromRequest(connection, jsoupRequestData);
    }

    public static String get(String url, JsoupRequestData jsoupRequestData, Map<String, String> cookies, int timeOut) {
        Connection connection = constructConnection(url, jsoupRequestData, cookies, timeOut, HttpsUtils.GET);
        return getHtmlFromRequest(connection, jsoupRequestData);

    }

    public static byte[] getBytes(String url, JsoupRequestData jsoupRequestData, Map<String, String> cookies, int timeout) {
        Connection connection = constructConnection(url, jsoupRequestData, cookies, timeout, HttpsUtils.GET);
        return getBytesFromRequest(connection, jsoupRequestData);
    }


    private static Connection constructConnection(String url, JsoupRequestData jsoupRequestData, Map<String, String> cookies, int timeOut, String method) {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            Connection connection = Jsoup
                    .connect(url)
                    .timeout(timeOut)

                    .userAgent(jsoupRequestData.getHeaders().get("User-Agent"));

            //set headers
            Map<String, String> headers = jsoupRequestData.getHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection = connection.header(entry.getKey(), entry.getValue());
            }

            if (cookies != null && !cookies.isEmpty()) {
                //set cookies
                connection = connection.cookies(cookies);
            }

            if(HttpsUtils.GET.equals(method)){
                connection = connection.method(Connection.Method.GET );
            }else if(HttpsUtils.POST.equals(method)){
                connection = connection.method(Connection.Method.POST);
                if(jsoupRequestData.getRequestData() instanceof String){
                    connection = connection.requestBody((String)jsoupRequestData.getRequestData());
                }else if(jsoupRequestData.getRequestData() instanceof Map){
                    connection = connection.data((Map<String,String>) jsoupRequestData.getRequestData());
                }else {
                    throw new RuntimeException("the request data format is not supported");
                }
            }else{
                throw new RuntimeException("the request method is not supported");
            }

            return connection;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private static byte[] getBytesFromRequest(Connection connection, JsoupRequestData jsoupRequestData) {
        try {
            Connection.Response response = connection.execute();
            int statusCode = response.statusCode();
            if (!jsoupRequestData.getStatusCodeSet().contains(statusCode)) {
                throw new ResponseStatusException("the response status code is not accepted! code: " + statusCode);
            }

            return IOUtils.toByteArray(response.bodyStream());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private static String getHtmlFromRequest(Connection connection, JsoupRequestData jsoupRequestData) {
        try {
            Connection.Response response = connection.execute();
            int statusCode = response.statusCode();
            if (!jsoupRequestData.getStatusCodeSet().contains(statusCode)) {
                throw new ResponseStatusException("the response status code is not accepted! code: " + statusCode);
            }

            String result = response.body();
            if (jsoupRequestData.isGetResponseHeaders()) {
                HashSetValuedHashMap<String, String> headers = new HashSetValuedHashMap<>();
                for (Map.Entry<String, List<String>> entry : response.multiHeaders().entrySet()) {
                    for (String value : entry.getValue()) {
                        headers.put(entry.getKey(), value);
                    }
                }

                result = headers + "\n" + response.body();
            }

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private static Document getDocumentFromRequest(Connection connection, JsoupRequestData jsoupRequestData) {
        try {
            Connection.Response response = connection.execute();
            int statusCode = response.statusCode();
            if (!jsoupRequestData.getStatusCodeSet().contains(statusCode)) {
                throw new ResponseStatusException("the response status code is not accepted! code: " + statusCode);
            }

            return response.parse();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


}
