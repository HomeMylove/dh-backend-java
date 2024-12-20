package com.homemylove.controller;

import com.homemylove.service.DownloadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/download")
@Slf4j
@AllArgsConstructor
public class DownloadController {

    private DownloadService downloadService;

    @GetMapping("/article/{id}")
    public ResponseEntity<byte[]> downloadArticle(@PathVariable("id") Long id,
                                                  HttpServletResponse response){
        return downloadService.downloadArticle(response,id);
    }

}
