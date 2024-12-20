package com.homemylove.service.impl;

import com.homemylove.core.api.R;
import com.homemylove.properties.ResourceProperties;
import com.homemylove.service.ArticleService;
import com.homemylove.service.DownloadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

@Service
@Slf4j
@AllArgsConstructor
public class DownloadServiceImpl implements DownloadService {

    private ArticleService articleService;

    @Override
    public ResponseEntity<byte[]> downloadArticle(HttpServletResponse response, Long id) {
        String path = articleService.getArticlePath(id);

        if (path != null) {
            File file = new File(path);
            try {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
                response.setContentLength((int) file.length());

                OutputStream outStream = response.getOutputStream();
                Files.copy(file.toPath(), outStream);
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 如果文件路径为空，返回404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return null;
    }
}
