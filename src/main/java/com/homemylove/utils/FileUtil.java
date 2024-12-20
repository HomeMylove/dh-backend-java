package com.homemylove.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileUtil {

    /**
     * 将文件读取为String
     * @return 文件不存在返回空
     */
    public static String readFileAsString(String path){
        if(!new File(path).exists()) {
            System.out.println("文件不存在");
            return "";
        }
        byte[] fileBytes = new byte[0];
        try {
            fileBytes = Files.readAllBytes(Paths.get(path));
            return new String(fileBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败:" + e.getMessage());
        }
    }

    /**
     * 获取 MultipartFile 类型文件的后缀
     * @param originalFilename 文件名
     * @return
     */
    public static String getFileExtension(String originalFilename) {
        if (originalFilename != null) {
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex != -1) {
                return originalFilename.substring(lastDotIndex + 1).toLowerCase();
            }
        }
        return null; // 如果没有后缀名，则返回null
    }
}
