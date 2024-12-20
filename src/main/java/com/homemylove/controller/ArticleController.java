package com.homemylove.controller;

import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.entity.Article;
import com.homemylove.service.ArticleService;
import com.homemylove.vo.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/search/{keyword}")
    public R<ListVo<ArticleSearchVo>> search(@PathVariable("keyword") String keyword, @RequestBody Query query) {
        return R.data(articleService.getArticleList(keyword, query));
    }

    @GetMapping("/details/{id}")
    public R<ArticleDetailVo> detail(@PathVariable("id") Long id) {
        return R.data(articleService.getArticleDetail(id));
    }

    @GetMapping("/hint/{hint}")
    public R<List<HintVo>> getArticleHint(@PathVariable("hint") String hint) {
        return R.data(articleService.getArticleHint(hint));
    }

    @PostMapping("/save")
    public R<String> saveArticle(@RequestBody Article article) {
        return articleService.saveArticle(article);
    }

    @PostMapping("/exists")
    public R<Boolean> articleExists(@RequestBody Article article) {
        return R.data(articleService.articleExists(article));
    }

    @GetMapping("/popularity")
    public R<List<Map<String, Object>>> getArticlePopularity() {
        return R.data(articleService.getArticlePopularity());
    }

    @GetMapping("/new")
    public R<List<ArticleCreateVo>> getLatestArticle() {
        return R.data(articleService.getLatestArticle());
    }

    /**
     * 获取某人上传的文章
     *
     * @param createUser
     * @param query
     * @return
     */
    @PostMapping("/upload_history/{id}")
    public R<ListVo<UploadArticleHistoryVo>> getUploadAuthorHistory(@PathVariable("id") Long createUser,
                                                                    @RequestBody Query query) {
        return R.data(articleService.getUploadArticleHistory(createUser, query));
    }

    @DeleteMapping("/{id}")
    public R<String> deleteArticle(@PathVariable("id") Long id) {
        return articleService.deleteArticle(id);
    }


}
