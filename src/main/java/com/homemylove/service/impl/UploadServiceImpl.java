package com.homemylove.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.homemylove.auth.AuthInfo;
import com.homemylove.core.api.R;
import com.homemylove.core.mdc.MDCScope;
import com.homemylove.properties.OssProperties;
import com.homemylove.properties.ResourceProperties;
import com.homemylove.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private OSS oss;

    @Resource
    private OssProperties ossProperties;

    @Resource
    private ResourceProperties resourceProperties;

    @PostConstruct
    private void init() {
        oss = new OSSClientBuilder()
                .build(ossProperties.getEndPoint(),
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret());
    }

    @Override
    public String upload(MultipartFile file, String fileName) {
        String bucketName = ossProperties.getBucketName();
        try {
            oss.putObject(bucketName, fileName, file.getInputStream());

            log.info("上传成功");
            return "https://" + bucketName + "."
                    + ossProperties.getEndPoint()
                    + "/" + fileName;
        } catch (OSSException | IOException e) {
            throw new RuntimeException("上传文件失败");
        }
    }

    @Override
    public R<String> uploadArticle(MultipartFile file, Long authorId, String title) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后上传");
        try {
            file.transferTo(getArticlePath(authorId, title));
            return R.success(new StringBuilder("./article/").
                    append(authorId).append("/").
                    append(title).append(".txt").toString());
        } catch (IOException e) {
            return R.fail("保存失败");
        }
    }

    @Override
    public R<String> uploadArticle(String text, Long authorId, String title) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后上传");
        try (FileWriter writer = new FileWriter(getArticlePath(authorId, title))) {
            // 写入字符串到文件
            writer.write(text);
            return R.success(new StringBuilder("./article/").
                    append(authorId).append("/").
                    append(title).append(".txt").toString());
        } catch (IOException e) {
            return R.fail("保存失败");
        }
    }

    private File getArticlePath(Long authorId, String title) {
        StringBuilder sb1 = new StringBuilder(resourceProperties.getPath()).append("/article/")
                .append(authorId);
        File directory = new File(sb1.toString());
        if (!directory.exists()) {
            // 如果文件夹不存在，创建它
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("创建失败");
            }
        }
        return new File(sb1.append("/").append(title).append(".txt").toString());
    }
}
