package com.homemylove.service;

import com.homemylove.core.api.R;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface DownloadService {
    ResponseEntity<byte[]> downloadArticle(HttpServletResponse response, Long id);
}
