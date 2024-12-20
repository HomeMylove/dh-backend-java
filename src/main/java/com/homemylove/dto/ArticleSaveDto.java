package com.homemylove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSaveDto {
    private Long authorId;
    private String title;
    private String text;
    private MultipartFile file;
    private TEXT_TYPE type;
    public enum TEXT_TYPE {
        TEXT, FILE;
    }
}
