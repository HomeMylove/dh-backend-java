package com.homemylove.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.homemylove.auth.AuthInfo;
import com.homemylove.core.api.R;
import com.homemylove.core.cs.CS;
import com.homemylove.core.entity.Query;
import com.homemylove.core.mdc.MDCScope;
import com.homemylove.core.mp.BaseServiceImpl;
import com.homemylove.entity.Article;
import com.homemylove.entity.Author;
import com.homemylove.entity.User;
import com.homemylove.mapper.ArticleMapper;
import com.homemylove.mapper.AuthorMapper;
import com.homemylove.mapper.UserMapper;
import com.homemylove.properties.ResourceProperties;
import com.homemylove.service.ArticleService;
import com.homemylove.utils.CollectionUtil;
import com.homemylove.utils.FileUtil;
import com.homemylove.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private AuthorMapper authorMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ResourceProperties resourceProperties;

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @Override
    public List<ArticleSearchVo> getArticleListByAuthorId(Long authorId) {
        LambdaQueryWrapper<Article> wrapper = Article.gw().select(Article::getId, Article::getTitle)
                .eq(Article::getAuthorId, authorId);

        List<Article> articles = articleMapper.selectList(wrapper);
        return articles.stream().map(a -> {
            ArticleSearchVo vo = new ArticleSearchVo();
            BeanUtils.copyProperties(a, vo, "deleted");
            return vo;
        }).toList();
    }

    @Override
    public ListVo<ArticleSearchVo> getArticleList(String keyword, Query query) {
        log.info("query:{}", query);
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<ArticleSearchVo> articleSearchVos = articleMapper.selectArticleList(keyword);
        PageInfo<ArticleSearchVo> pageInfo = new PageInfo<>(articleSearchVos);
        return new ListVo<>(articleSearchVos, pageInfo.getTotal());
    }

    @Override
    public ArticleDetailVo getArticleDetail(Long id) {
        LambdaQueryWrapper<Article> wrapper = Article.gw().select(Article::getId, Article::getTitle, Article::getPath, Article::getAuthorId, Article::getCreateUser)
                .eq(Article::getId, id);

        Article article = articleMapper.selectOne(wrapper);
        ArticleDetailVo articleVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article, articleVo);
        // 获取内容
        String realPath = getRealPath(article);
        String text = FileUtil.readFileAsString(realPath);

        articleVo.setText(text);
        // 获取作者
        Author author = authorMapper.selectById(article.getAuthorId());
        AuthorSearchVo authorSearchVo = new AuthorSearchVo();
        authorSearchVo.setId(author.getId());
        authorSearchVo.setName(author.getKanjiName());
        articleVo.setAuthor(authorSearchVo);
        // 获取创造者
        Long userId = article.getCreateUser();
        if (userId != null) {
            ArticleDetailVo.CreatorInfo creator = new ArticleDetailVo.CreatorInfo();
            User one = userMapper.selectOne(User.gw().select(User::getUsername).eq(User::getId, userId));
            creator.setId(userId);
            creator.setName(one.getUsername());
            articleVo.setCreator(creator);
        }

        // 保存 count 到redis
        redisTemplate.opsForZSet().incrementScore(CS.REDIS_KEY.ARTICLE_POPULARITY.getKey(), id, 1);
        return articleVo;
    }

    @Override
    public List<HintVo> getArticleHint(String hint) {
        PageHelper.startPage(1, 10);
        LambdaQueryWrapper<Article> wrapper = Article.gw()
                .select(Article::getTitle, Article::getId)
                .likeRight(Article::getTitle, hint);
        return articleMapper.selectList(wrapper)
                .stream().map(a -> new HintVo(a.getId(), a.getTitle())).toList();
    }

    @Override
    public R<String> saveArticle(Article article) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后上传");
        boolean save = save(article);
        if (save) return R.success("上传成功");
        return R.fail("上传失败");
    }

    @Override
    public Boolean articleExists(Article article) {
        return !articleMapper.selectList(Article.gw().select(Article::getId)
                .eq(Article::getTitle, article.getTitle())
                .eq(Article::getAuthorId, article.getAuthorId())).isEmpty();
    }

    @Override
    public void updateArticlePopularity() {
        String pk = CS.REDIS_KEY.ARTICLE_POPULARITY.getKey();
        String snapshot = CS.REDIS_KEY.ARTICLE_POPULARITY_SNAPSHOT.getKey();

        // 获取长度
        Long size = redisTemplate.opsForZSet().size(pk);
        if (size == null) return;
        long end = size > 10 ? 9 : -1L;
        Set<Long> longs = redisTemplate.opsForZSet().reverseRange(pk, 0, end);
        // 存储到 snapshot
        assert longs != null;

        redisTemplate.delete(snapshot);
        CollectionUtil.transformToList(longs)
                .forEach(a -> redisTemplate.opsForList().rightPush(snapshot, a));
    }

    @Override
    public List<Map<String, Object>> getArticlePopularity() {
        List<Long> articleIds = redisTemplate.opsForList().range(CS.REDIS_KEY.ARTICLE_POPULARITY_SNAPSHOT.getKey(), 0, -1);
        assert articleIds != null;
        return CollectionUtil.transformToList(articleIds).stream().map(a -> {
            Map<String, Object> map = new HashMap<>();
            Article article = articleMapper.selectOne(Article.gw().select(Article::getTitle).eq(Article::getId, a));
            if (article == null) return null;
            map.put("id", a);
            map.put("name", article.getTitle());
            return map;
        }).filter(Objects::nonNull).toList();
    }

    @Override
    public List<ArticleCreateVo> getLatestArticle() {
        return articleMapper.getLatestArticle(10);
    }

    @Override
    public String getArticlePath(Long id) {
        Article article = articleMapper.selectOne(Article.gw().select(Article::getPath).eq(Article::getId, id));
        return article == null ? null : getRealPath(article);
    }

    @Override
    public ListVo<UploadArticleHistoryVo> getUploadArticleHistory(Long createUser, Query query) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        // 查询
        List<Article> articles = articleMapper.selectList(Article.gw().select(Article::getId, Article::getTitle, Article::getAuthorId, Article::getCreateTime)
                .eq(Article::getCreateUser, createUser).orderByDesc(Article::getId));
        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        boolean deletable = authInfo != null && Objects.equals(authInfo.getId(), createUser);

        List<UploadArticleHistoryVo> historyVos = articles.stream().map(a -> {
            UploadArticleHistoryVo historyVo = new UploadArticleHistoryVo();
            historyVo.setId(a.getId());
            historyVo.setName(a.getTitle());
            historyVo.setCreateTime(a.getCreateTime());
            historyVo.setDeletable(deletable);
            //  设置 author
            Author author = authorMapper.selectOne(Author.gw().select(Author::getKanjiName).eq(Author::getId, a.getAuthorId()));
            AuthorInfoVo infoVo = new AuthorInfoVo(a.getAuthorId(), author.getKanjiName());
            historyVo.setAuthor(infoVo);
            return historyVo;
        }).toList();
        return new ListVo<>(historyVos, pageInfo.getTotal());
    }

    @Override
    public R<String> deleteArticle(Long id) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后可删除");

        int delete = articleMapper.delete(Article.gw().eq(Article::getId, id).eq(Article::getCreateUser, authInfo.getId()));
        if (delete > 0) {
            return R.success("删除成功");
        } else return R.fail("删除失败");

    }

    private String getRealPath(Article article) {
        // 去除yml中路径后面的东西
        String basePath = resourceProperties.getPath();
        int lastIndex = basePath.length() - 1;
        String lastWord = basePath.substring(lastIndex);
        if (lastWord.equals("/")) {
            basePath = basePath.substring(0, lastIndex);
        }

        // 去除数据库中路径前面的东西
        String path = article.getPath().replaceFirst("./", "");
        return basePath + "/" + path;
    }
}
