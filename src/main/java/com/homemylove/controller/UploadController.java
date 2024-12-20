package com.homemylove.controller;

import com.homemylove.config.OssFileConfig;
import com.homemylove.core.api.R;
import com.homemylove.service.UploadService;
import com.homemylove.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@Slf4j
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/{bizType}")
    public R<String> fileUpload(@RequestParam("file") MultipartFile file,
                                @PathVariable("bizType") String bizType) {

        if (!OssFileConfig.isAllowBizType(bizType)) {
            return R.fail("不支持的类型");
        }
        String suffix = FileUtil.getFileExtension(file.getOriginalFilename());
        if (!OssFileConfig.isAllowFileSuffix(suffix)) {
            return R.fail("不支持的后缀");
        }
        if (!OssFileConfig.isMaxSizeLimit(file.getSize())) {
            return R.fail("文件大小超过限制");
        }
        String saveDirAndFileName = bizType + "/" + UUID.randomUUID() + "." + suffix;
        String url = uploadService.upload(file, saveDirAndFileName);
        return R.data(url);
    }

    @PostMapping("/save_article_file/{authorId}/{title}")
    public R<String> saveFileArticle(@RequestBody MultipartFile file,
                                     @PathVariable("authorId") Long authorId,
                                     @PathVariable("title") String title) {
        return uploadService.uploadArticle(file, authorId, title);
    }

    @PostMapping("/save_article_text/{authorId}/{title}")
    public R<String> saveTextArticle(@RequestBody Map<String, Object> data,
                                     @PathVariable("authorId") Long authorId,
                                     @PathVariable("title") String title) {
        return uploadService.uploadArticle((String) data.get("text"), authorId, title);
    }



}
