package com.homemylove.service;

import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.vo.ListVo;
import com.homemylove.vo.UploadHistoryVo;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    /**
     * 上传文件，返回地址
     * @param file
     * @param fileName
     * @return
     */
    String upload(MultipartFile file,String fileName);

    R<String> uploadArticle(MultipartFile file, Long authorId, String title);

    R<String> uploadArticle(String text,Long authorId,String title);

}
