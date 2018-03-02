package com.kxxfydj.webmagicext;

import com.kxxfydj.utils.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 图片、文件的下载器
 * @author wangyonghuang
 * @date 2017-2-8
 */
public class ByteHttpClientDownloader extends HttpClientDownloader {
	private static final Logger log = getLogger(ByteHttpClientDownloader.class);

	public ByteHttpClientDownloader() {
		try {
			Field httpClientGeneratorField = HttpClientDownloader.class.getDeclaredField("httpClientGenerator");
			httpClientGeneratorField.setAccessible(true);
			httpClientGeneratorField.set(this, new MyHttpClientGenerator());
		} catch (Exception e) {
			log.error("Init ByteHttpClientDownloader occur error! ",e);
		}
	}
	
	@Override
	protected String getContent(String charset, HttpResponse httpResponse) throws IOException {
         byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
         return Base64.encode(contentBytes);
    }
}
