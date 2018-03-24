import com.kxxfydj.utils.CreateFileUtil;
import com.kxxfydj.utils.HeaderUtils;
import com.kxxfydj.utils.HttpsUtils;
import com.kxxfydj.utils.JsoupRequestData;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by kxxfydj on 2018/3/2.
 */
public class DownloadTest {

    private static final Logger logger = LoggerFactory.getLogger(DownloadTest.class);

    private static Map<String, String> requestHeaderMap;

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    String PROJECT_PATH = "D:\\codeSource";

    private static final String HOST = "codeload.github.com";

    private static final String REFERER = "https://github.com";

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
//        downloadZip("java","ZZZZZ","https://github.com/TheAlgorithms/Java/archive/master.zip");
            CreateFileUtil.generateFile("D:\\codeSource\\github\\Java\\zzzz.txt","sdjlfsdfdsfs");

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
        jsoupRequestData.setFiddlerProxy();
        jsoupRequestData.setHeaders(requestHeaderMap);
//            apacheHttpRequestData.setFiddlerProxy();
        byte[] binaryData = HttpsUtils.getBytes(downloadPath, jsoupRequestData, null);
        CreateFileUtil.generateFile(filePath, binaryData);
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
            Map<String,String> header = HeaderUtils.initGetHeaders("codeload.github.com","https://github.com","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36");

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

    /**
     * 解压缩zip包
     *
     * @param zipFilePath        zip文件的全路径
     * @param unzipFilePath      解压后的文件保存的路径
     * @param includeZipFileName 解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含
     */
    @SuppressWarnings("unchecked")
    public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception {
        if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath)) {
            throw new Exception("zip解压出错！");
        }
        File zipFile = new File(zipFilePath);
        //如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径
        if (includeZipFileName) {
            String fileName = zipFile.getName();
            if (StringUtils.isNotEmpty(fileName)) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            unzipFilePath = unzipFilePath + File.separator + fileName;
        }
        //创建解压缩文件保存的路径
        File unzipFileDir = new File(unzipFilePath);
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
            unzipFileDir.mkdirs();
        }

        //开始解压
        ZipEntry entry = null;
        String entryFilePath = null, entryDirPath = null;
        File entryFile = null, entryDir = null;
        int index = 0, count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        //循环对压缩包里的每一个文件进行解压
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            //构建压缩包中一个文件解压后保存的文件全路径
            entryFilePath = unzipFilePath + File.separator + entry.getName();
            //构建解压后保存的文件夹路径
            index = entryFilePath.lastIndexOf(File.separator);
            if (index != -1) {
                entryDirPath = entryFilePath.substring(0, index);
            } else {
                entryDirPath = "";
            }
            entryDir = new File(entryDirPath);
            //如果文件夹路径不存在，则创建文件夹
            if (!entryDir.exists() || !entryDir.isDirectory()) {
                entryDir.mkdirs();
            }

            //创建解压文件
            entryFile = new File(entryFilePath);
            if (entryFile.exists()) {
                //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
                SecurityManager securityManager = new SecurityManager();
                securityManager.checkDelete(entryFilePath);
                //删除已存在的目标文件
                entryFile.delete();
            }

            //写入文件
            bos = new BufferedOutputStream(new FileOutputStream(entryFile));
            bis = new BufferedInputStream(zip.getInputStream(entry));
            while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                bos.write(buffer, 0, count);
            }
            bos.flush();
            bos.close();
        }
    }
}
