package com.kxxfydj.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {
	private static final RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000).setConnectTimeout(30000).setSocketTimeout(30000).build();
	
	private static final String userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

	private static final CloseableHttpClient httpclient = HttpClients.custom().setUserAgent(userAgent).setMaxConnTotal(100).setMaxConnPerRoute(100).setDefaultRequestConfig(requestConfig).build();

	private static final ResponseHandler<StringResponse> responseHandler = new ResponseHandler<StringResponse>() {
		@Override
		public StringResponse handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			StringResponse stringResponse = new StringResponse();
			stringResponse.setStatusCode(status);
			ContentType ct = ContentType.get(entity);
			Charset charset = ct != null ? ct.getCharset() : null;
			if (charset == null) {
				charset = Charset.defaultCharset();
			}
			stringResponse.setCharset(charset);
			stringResponse.setResponseBody(entity != null ? EntityUtils.toString(entity, charset) : null);
			return stringResponse;

		}

	};

	/**
	 * 处理resepone返回字节类型的结果
	 */
	private static final ResponseHandler<BinaryResponse> binaryResponseHandler = new ResponseHandler<BinaryResponse>() {
		@Override
		public BinaryResponse handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			BinaryResponse myResponse = new BinaryResponse();
			myResponse.setStatusCode(status);
			ContentType contentType = ContentType.get(entity);
			String mimeType = null;
			if (contentType != null) {
				mimeType = ContentType.get(entity).getMimeType();
			}

			myResponse.setMimeType(mimeType);
			byte[] bytes = entity != null ? EntityUtils.toByteArray(entity) : null;
			myResponse.setResponseBody(bytes);

			return myResponse;

		}

	};

	public static StringResponse get(String url) throws Exception {

		HttpGet httpget = new HttpGet(url);
		return httpclient.execute(httpget, responseHandler);

	}

	public static StringResponse get(String url, String charSet) throws Exception {

		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);
		int status = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();
		StringResponse stringResponse = new StringResponse();
		stringResponse.setStatusCode(status);
		Charset charset = Charset.forName(charSet);
		stringResponse.setCharset(charset);
		stringResponse.setResponseBody(entity != null ? EntityUtils.toString(entity, charset) : null);
		return stringResponse;

	}

	public static StringResponse getGbk(String url) throws Exception {
		return get(url, "gbk");

	}

	public static StringResponse post(String url) throws Exception {

		HttpPost httppost = new HttpPost(url);
		return httpclient.execute(httppost, responseHandler);

	}

	public static StringResponse post(String url, Map<String, String> paramMap) throws Exception {

		HttpPost httppost = new HttpPost(url);

		List<NameValuePair> nps = map2NameValuePairs(paramMap);
		UrlEncodedFormEntity en = new UrlEncodedFormEntity(nps, Consts.UTF_8);
		en.setContentEncoding("UTF-8");
		httppost.setEntity(en);

		return httpclient.execute(httppost, responseHandler);

	}

	public static StringResponse json(String url, String body) throws Exception {

		HttpPost method = new HttpPost(url);
		StringEntity stringEntity = new StringEntity(body, Consts.UTF_8);
		stringEntity.setContentEncoding("UTF-8");
		stringEntity.setContentType("application/json");
		method.setEntity(stringEntity);
		return httpclient.execute(method, responseHandler);

	}

	public static List<NameValuePair> map2NameValuePairs(final Map<String, String> params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (params == null) {
			return nameValuePairs;
		}
		for (Entry<String, String> entry : params.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return nameValuePairs;
	}

	public static BinaryResponse getBinary(String url) throws Exception {
		HttpGet get = new HttpGet(url);
		BinaryResponse myResponse = httpclient.execute(get, binaryResponseHandler);
		return myResponse;
	}

	public static void main(String[] args) {
		try {
			post("http://127.0.0.1:8080/cardniu-monitor/login/doLogin?userName=fujinlong");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
