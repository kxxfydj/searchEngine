package com.kxxfydj.utils;

import com.kxxfydj.crawlerConfig.CrawlerConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by kxxfydj on 2018/3/30.
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 删除指定目录下的文件，包括当前的文件夹
     *
     * @param filePath 文件路径
     * @return 成功返回true，如果过程中任意一个文件删除失败，返回false
     */
    public static boolean deleteFiles(String filePath) {
        boolean flag = true;
        File file = new File(filePath);
        if (!file.exists()) {
            return !flag;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }
        String[] tempList = file.list();
        File temp = null;
        for (String aTempList : tempList) {
            if (filePath.endsWith(File.separator)) {
                temp = new File(filePath + aTempList);
            } else {
                temp = new File(filePath + File.separator + aTempList);
            }
            if (temp.isFile()) {
                flag = flag && temp.delete();
            } else if (temp.isDirectory()) {
//                boolean tempFlag = deleteFiles(filePath + File.separator + aTempList);//先删除文件夹里面的文件
                flag = flag && deleteFiles(filePath + File.separator + aTempList);//先删除文件夹里面的文件
            }
        }
        flag = flag && file.delete();
        return flag;
    }

    /**
     * 将String字符串更新到源文件，如果该文件不存在则新建
     * @return 失败false;成功true
     */
    public static boolean updateFiles(String filePath,String content){
        return updateFiles(filePath, content.getBytes());
    }

    /**
     * 将二进制数据更新到源文件，如果该文件不存在则新建
     * @return 失败false;成功true
     */
    public static boolean updateFiles(String filePath,byte[] content) {
        boolean flag = true;
        if(StringUtils.isBlank(filePath)){
            logger.error("文件路径为空！");
            return !flag;
        }
        File file = new File(filePath);
        if(file.isDirectory()){
            logger.error("文件路径为文件夹路径！");
            return !flag;
        }
        try {
            org.apache.commons.io.FileUtils.writeByteArrayToFile(file,content);
        } catch (IOException e) {
            logger.error("文件内容更新出错！",e.getMessage(),e);
            flag = false;
        }
        return flag;
    }

    /**
     * 解压指定目录下的所有zip文件，输出到同一个文件夹下
     *
     * @param filePath 指定目标文件夹
     * @return 成功返回true，如果其中任何一个文件解压失败，返回false
     */
    public static boolean unzipFiles(String filePath, String unzipPath) {
        File file = new File(filePath);
        boolean flag = true;
        if (file.isFile()) {
            if (file.getAbsolutePath().endsWith(".zip")) {
                flag = flag && unzip(filePath, unzipPath, true);
                //unzip(filePath, filePath, true)
            }
        }
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isFile() && childFile.getAbsolutePath().endsWith(".zip")) {
                flag = flag && unzip(childFile.getAbsolutePath(), unzipPath, true);
            } else if(childFile.isDirectory()){
                flag = flag && unzipFiles(childFile.getAbsolutePath(), unzipPath);
            }
        }
        return flag;
    }

    /**
     * 解压缩zip包
     *
     * @param zipFilePath        zip文件的全路径
     * @param unzipFilePath      解压后的文件保存的路径
     * @param includeZipFileName 解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含
     */
    @SuppressWarnings("unchecked")
    public static boolean unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) {
        if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath)) {
            logger.error("zip解压出错！文件目录为空！");
            return false;
        }
        boolean flag = true;
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
        String entryFilePath;
        String entryDirPath;
        File entryFile = null;
        File entryDir;
        int index;
        int count;
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try (
                ZipFile zip = new ZipFile(zipFile)
        ) {
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
                    logger.info("删除文件：{}", entryFile.getAbsolutePath());
                    deleteFiles(entryFile.getAbsolutePath());
//                    //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
//                    SecurityManager securityManager = new SecurityManager();
//                    securityManager.checkDelete(entryFilePath);
//                    //删除已存在的目标文件
//                    flag = flag && entryFile.delete();
                }
                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    try (BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
                         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(entryFile))) {
                        //写入文件
                        while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                            bos.write(buffer, 0, count);
                        }
                        bos.flush();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("解压文件出错！zipFile:{} name:{}", zipFilePath, zipFile.getName(), e.getMessage(), e);
            flag = false;
        }
        return flag;
    }
}
