import com.kxxfydj.utils.FileUtils;
import com.kxxfydj.utils.HeaderUtils;
import com.kxxfydj.utils.HttpsUtils;
import com.kxxfydj.utils.JsoupRequestData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

/**
 * Created by kxxfydj on 2018/3/2.
 */
public class DownloadTest {

    private static final Logger logger = LoggerFactory.getLogger(DownloadTest.class);

    private static Map<String, String> requestHeaderMap;

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    String PROJECT_PATH = "D:\\codeSource";

    private static final String HOST = "gitlab.com";

    private static final String REFERER = "https://gitlab.com";

    private static final String USERAGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36";

    static {
        requestHeaderMap = HeaderUtils.initPostHeaders("github.com",
                "https://github.com/",
                "application/x-www-form-urlencoded",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");
    }

    @Test
    public void test() {
//        File file = new File("C:\\Users\\kxxfydj\\Desktop\\theAlgorithms.zip");
//
//        try(FileOutputStream bos = new FileOutputStream(file)) {
//            ApacheHttpRequestData apacheHttpRequestData = new ApacheHttpRequestData();
//            apacheHttpRequestData.setHeaders(requestHeaderMap);
//            apacheHttpRequestData.setFiddlerProxy();
//            byte[] binaryData = ApacheFetchUtils.getBytes(apacheHttpRequestData, "https://codeload.github.com/TheAlgorithms/Java/zip/master");
//
//            bos.write(binaryData);
//            bos.flush();
//        }catch (Exception e){
//            logger.error(e.getMessage(),e);
//        }
//        System.out.println(System.getProperty("user.dir"));
        downloadZip("java", "ZZZZZ", "https://gitlab.com/gitlab-org/gitlab-ce/-/archive/master/gitlab-ce-master.zip");
        FileUtils.generateFile("D:\\codeSource\\github\\Java\\zzzz.txt", "sdjlfsdfdsfs");
//        Class<GitLabCrawler> clazz = GitLabCrawler.class;
//        Constructor<?>[] constructors = clazz.getConstructors();
//        for(Constructor<?> constructor : constructors){
//            System.out.println(constructor.getParameterCount());
//        }

    }


    private void downloadZip(String language, String projectName, String downloadPath) {
//        downloadPath = downloadPath.replaceAll("archive", "zip");
//        downloadPath = downloadPath.substring(0, downloadPath.lastIndexOf(".zip"));
//        downloadPath = downloadPath.replaceAll("github\\.com", "codeload.github.com");
        String filePath = PROJECT_PATH + FILE_SEPARATOR + "github" + FILE_SEPARATOR + language + FILE_SEPARATOR + projectName + ".zip";
//        File file = new File(filePath);

        Map<String, String> requestHeaderMap;
        requestHeaderMap = HeaderUtils.initGetHeaders(HOST, REFERER, USERAGENT);
        JsoupRequestData jsoupRequestData = new JsoupRequestData();
//        jsoupRequestData.setFiddlerProxy();
        jsoupRequestData.setTimeOut(0);
        jsoupRequestData.setHeaders(requestHeaderMap);
        byte[] binaryData = HttpsUtils.getBytes(downloadPath, jsoupRequestData, null);
        FileUtils.generateFile(filePath, binaryData);
    }

    @Test
    public void testSearchGitHub() {

        try {
//            System.setProperty("javax.net.debug", "all");
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

            // Fetch url
            String url = "https://codeload.github.com/TheAlgorithms/Java/zip/master";

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888));
            Map<String, String> header = HeaderUtils.initGetHeaders("codeload.github.com", "https://github.com", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");

            Connection.Response response = Jsoup //
                    .connect(url) //
                    .timeout(60000) //
                    .method(Connection.Method.GET) //
                    .headers(header)
                    .ignoreContentType(true)
                    .proxy(proxy)
                    .execute();


//            Document document = response.parse();
            byte[] data = response.bodyAsBytes();
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testaaa() {
        ThreadMM threadMM = new ThreadMM();
        Thread aa = new Thread(threadMM);
        threadMM.start();
        aa.start();
    }

    class ThreadMM extends Thread implements Runnable {
        @Override
        public void run() {
            System.out.println("thread:" + Thread.currentThread().getName());
        }
    }

}
