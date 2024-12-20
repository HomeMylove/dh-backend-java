package com.homemylove.service;

import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.entity.Author;
import com.homemylove.vo.*;

import java.util.List;
import java.util.Map;


public interface AuthorService {
    /**
     * 根据关键词查询作家
     * @param keyword 关键词 模糊查询
     * @param query 分页参数
     * @return
     */
    ListVo<AuthorSearchVo> getAuthorList(String keyword, Query query);

    AuthorDetailVo getAuthorDetail(Long id);

    /**
     * 获取作者提示
     * @param hint
     * @return
     */
    List<HintVo> getAuthorHint(String hint);


    Boolean checkAuthorName(String name);

    R<String> saveAuthor(Author author);

    Long getAuthorIdByName(String name);

    List<Map<String, Object>> getAuthorPopularity();

    void updateAuthorPopularity();

    ListVo<UploadAuthorHistoryVo> getUploadAuthorHistory(Long createUser, Query query);

    R<String> deleteAuthor(Long id);
}
