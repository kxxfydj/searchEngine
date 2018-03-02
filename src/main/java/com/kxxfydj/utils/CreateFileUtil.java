package com.kxxfydj.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * create by kaiming_xu on 2017/9/2
 */
public class CreateFileUtil {

    private CreateFileUtil() {

    }

    public static void generateFile(String path, String content) {
        if (StringUtils.isBlank(path)) {
            return;
        }

        String folderPath = path.substring(0, path.lastIndexOf(File.separator));
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }


        try (FileOutputStream fileout = new FileOutputStream(path);
             BufferedOutputStream out = new BufferedOutputStream(fileout)
        ) {
            out.write(content.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateFile(String path, byte[] content) {
        if (StringUtils.isBlank(path)) {
            return;
        }

        String folderPath = path.substring(0, path.lastIndexOf(File.separator));
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        try (FileOutputStream fileout = new FileOutputStream(path);
             BufferedOutputStream out = new BufferedOutputStream(fileout)
        ) {
            out.write(content);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
