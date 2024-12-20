package com.homemylove.service;

import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.entity.FavList;
import com.homemylove.vo.ArticleFavVo;
import com.homemylove.vo.ArticleSearchVo;
import com.homemylove.vo.FavListVo;
import com.homemylove.vo.ListVo;

import java.util.List;
import java.util.Map;

public interface FavoriteService {
    R<String> editFavList(FavList favList);
    List<FavListVo> getFavList(Map<String,Object> data);

    R<String> deleteFavList(Long id);

    R<String> addArticleToFavList(Map<String, Object> data);

    ListVo<ArticleFavVo> getFavArticles(Long id, Query query);

    R<String> deleteFavArticle(Long id);
}
