package com.homemylove.controller;

import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.entity.FavList;
import com.homemylove.service.FavoriteService;
import com.homemylove.vo.ArticleFavVo;
import com.homemylove.vo.FavListVo;
import com.homemylove.vo.ListVo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
@AllArgsConstructor
public class FavoriteController {

    private FavoriteService favoriteService;

    @PostMapping("/editList")
    public R<String> editFavList(@RequestBody FavList favList){
        return favoriteService.editFavList(favList);
    }

    /**
     * 获取收藏列表
     * @return
     */
    @PostMapping("/favList")
    public R<List<FavListVo>> getFavList(@RequestBody(required = false) Map<String,Object> data){
        return R.data(favoriteService.getFavList(data));
    }

    @DeleteMapping("/favList/{id}")
    public R<String> delFavList(@PathVariable("id") Long id){
        return favoriteService.deleteFavList(id);
    }

    @PostMapping("/article")
    public R<String> addArticleToFavList(@RequestBody Map<String,Object> data){
        return favoriteService.addArticleToFavList(data);
    }

    /**
     * 获取某个收藏的文章
     * id 收藏夹的 id
     */
    @PostMapping("/article/{id}")
    public R<ListVo<ArticleFavVo>> getFavArticles(@PathVariable("id") Long id,
                                                  @RequestBody Query query){

        return R.data(favoriteService.getFavArticles(id,query));
    }

    @DeleteMapping("/article/{id}")
    public R<String> deleteFavArticle(@PathVariable("id") Long id){
        return favoriteService.deleteFavArticle(id);
    }

}
