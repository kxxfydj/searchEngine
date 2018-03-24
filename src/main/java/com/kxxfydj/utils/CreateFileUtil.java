package com.kxxfydj.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class CreateFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(CreateFileUtil.class);

    private CreateFileUtil() {

    }

    public static void generateFile(String path, String content) {
        generateFile(path,content.getBytes());
    }

    public static void generateFile(String path, byte[] content) {
        if (StringUtils.isBlank(path) || content == null) {
            logger.error("文件路径为空，或者二进制数据为空   文件路径：{}", path);
            return;
        }

        String folderPath = path.substring(0, path.lastIndexOf(File.separator));
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if(!folder.mkdirs()){
                logger.error("生成文件夹出错! 文件路径：{} 文件全路径名：{}", folderPath, path);
                return;
            }
        }

        File file = new File(path);
        if(file.exists()){
            if(!file.delete()){
                logger.error("删除文件出错！ 文件全路径名：{}",path);
            }
        }

        try (FileOutputStream fileout = new FileOutputStream(path);
             BufferedOutputStream out = new BufferedOutputStream(fileout)
        ) {
            out.write(content);
            out.flush();
        } catch (IOException e) {
            logger.error("生成文件出错",e.getMessage(),e);
        }
    }

}
