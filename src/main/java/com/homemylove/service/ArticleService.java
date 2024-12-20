package com.homemylove.service;

import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.entity.Article;
import com.homemylove.vo.*;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    List<ArticleSearchVo> getArticleListByAuthorId(Long authorId);

    ListVo<ArticleSearchVo> getArticleList(String keyword, Query query);

    ArticleDetailVo getArticleDetail(Long id);

    List<HintVo> getArticleHint(String hint);

    R<String> saveArticle(Article article);

    Boolean articleExists(Article article);

    void updateArticlePopularity();

    List<Map<String, Object>> getArticlePopularity();

    List<ArticleCreateVo> getLatestArticle();

    String getArticlePath(Long id);

    ListVo<UploadArticleHistoryVo> getUploadArticleHistory(Long createUser, Query query);

    R<String> deleteArticle(Long id);
}
