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
import com.homemylove.service.ArticleService;
import com.homemylove.service.AuthorService;
import com.homemylove.utils.CollectionUtil;
import com.homemylove.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
@Slf4j
public class AuthorServiceImpl extends BaseServiceImpl<AuthorMapper, Author> implements AuthorService {

    @Resource
    private AuthorMapper authorMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleService articleService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String,Long> redisTemplate;

    @Override
    public ListVo<AuthorSearchVo> getAuthorList(String keyword, Query query) {
        // 分页
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Author> wrapper = Author.gw().like(Author::getKanaName, keyword).or().like(Author::getKanjiName, keyword)
                .or().like(Author::getRomanName, keyword)
                .select(Author::getId, Author::getAbout, Author::getKanjiName,Author::getCreateUser);

        List<Author> authors = authorMapper.selectList(wrapper);
        PageInfo<Author> pageInfo = new PageInfo<>(authors);
        // 映射
        List<AuthorSearchVo> authorSearchVos = authors.stream().map(a -> {
            AuthorSearchVo vo = new AuthorSearchVo();
            BeanUtils.copyProperties(a, vo);
            vo.setName(a.getKanjiName());
            // 设置创建者
            Long userId = a.getCreateUser();
            if(userId != null){
                User one = userMapper.selectOne(User.gw().select(User::getUsername).eq(User::getId, userId));
                vo.setCreator(new CreatorInfoVo(userId,one.getUsername()));
            }

            return vo;
        }).toList();
        return new ListVo<AuthorSearchVo>(authorSearchVos, pageInfo.getTotal());
    }

    @Override
    public AuthorDetailVo getAuthorDetail(Long id) {
        AuthorDetailVo vo = new AuthorDetailVo();
        BeanUtils.copyProperties(authorMapper.selectById(id), vo);
        vo.setArticles(articleService.getArticleListByAuthorId(id));

        // 保存记录到 redis
        redisTemplate.opsForZSet().incrementScore(CS.REDIS_KEY.AUTHOR_POPULARITY.getKey(),id ,1);
        return vo;
    }

    @Override
    public List<HintVo> getAuthorHint(String hint) {
        PageHelper.startPage(1, 10);
        LambdaQueryWrapper<Author> wrapper = Author.gw()
                .likeRight(Author::getKanaName, hint).or().likeRight(Author::getKanjiName, hint)
                .or().likeRight(Author::getRomanName, hint)
                .select(Author::getKanjiName, Author::getId);
        List<Author> authors = authorMapper.selectList(wrapper);
        return authors.stream().map(a -> new HintVo(a.getId(), a.getKanjiName())).toList();
    }

    @Override
    public Boolean checkAuthorName(String name) {
        if(name.isBlank()) return true;
        // 检查作家名在不在
        List<Author> authors = authorMapper.selectList(
                Author.gw()
                        .eq(Author::getKanjiName, name).or()
                        .eq(Author::getKanaName, name).or()
                        .eq(Author::getRomanName, name)
                        .select(Author::getId)
        );
        return !authors.isEmpty();
    }

    @Override
    public R<String> saveAuthor(Author author) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if(authInfo == null) return R.fail("登陆后上传");

        List<String> names = List.of(author.getKanjiName(), author.getKanaName(), author.getRomanName());
        boolean match = names.stream().anyMatch(n -> {
            if (n.isBlank()) return true;
            return !checkAuthorName(n);
        });
        if(!match) return R.fail("作家已存在");
        if(save(author)) return R.success("上传成功");
        return R.fail("上传失败");
    }

    @Override
    public Long getAuthorIdByName(String name) {
        Author author = authorMapper.selectOne(Author.gw().select(Author::getId).eq(Author::getKanjiName, name));
        if(author == null) return -1L;
        return author.getId();
    }

    @Override
    public List<Map<String, Object>> getAuthorPopularity() {
        List<Long> authorIds = redisTemplate.opsForList().range(CS.REDIS_KEY.AUTHOR_POPULARITY_SNAPSHOT.getKey(), 0, -1);

        assert authorIds != null;
        log.info("athorIds:{}",authorIds);

        return CollectionUtil.transformToList(authorIds).stream().map(a -> {
            Map<String, Object> map = new HashMap<>();
            Author author = authorMapper.selectOne(Author.gw().select(Author::getKanjiName).eq(Author::getId, a));
            if (author == null) return null;
            map.put("id", a);
            map.put("name", author.getKanjiName());
            return map;
        }).filter(Objects::nonNull).toList();
    }

    @Override
    public void updateAuthorPopularity() {
        String pk = CS.REDIS_KEY.AUTHOR_POPULARITY.getKey();
        String snapshot = CS.REDIS_KEY.AUTHOR_POPULARITY_SNAPSHOT.getKey();

        // 获取长度
        Long size = redisTemplate.opsForZSet().size(pk);
        if(size == null) return;
        long end = size > 10 ? 9 : -1L;
        Set<Long> longs = redisTemplate.opsForZSet().reverseRange(pk, 0, end);
        log.info("update:{}",longs);
        // 存储到 snapshot
        assert longs != null;

        redisTemplate.delete(snapshot);
        CollectionUtil.transformToList(longs)
                .forEach(a->redisTemplate.opsForList().rightPush(snapshot,a));

    }

    @Override
    public ListVo<UploadAuthorHistoryVo> getUploadAuthorHistory(Long createUser, Query query) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        // 查询
        List<Author> authors = authorMapper.selectList(Author.gw().select(Author::getId, Author::getKanjiName, Author::getCreateTime)
                .eq(Author::getCreateUser, createUser).orderByDesc(Author::getId));
        PageInfo<Author> pageInfo = new PageInfo<>(authors);

        boolean deletable = authInfo != null && Objects.equals(authInfo.getId(), createUser);
        List<UploadAuthorHistoryVo> historyVos = authors.stream().map(a -> {
            UploadAuthorHistoryVo historyVo = new UploadAuthorHistoryVo();
            historyVo.setId(a.getId());
            historyVo.setName(a.getKanjiName());
            historyVo.setCreateTime(a.getCreateTime());
            historyVo.setDeletable(deletable);
            return historyVo;
        }).toList();
        return new ListVo<>(historyVos,pageInfo.getTotal());
    }

    @Override
    public R<String> deleteAuthor(Long id) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后可删除");

        int delete = authorMapper.delete(Author.gw().eq(Author::getId, id).eq(Author::getCreateUser, authInfo.getId()));
        // 同时把作品删了
        articleMapper.delete(Article.gw().eq(Article::getAuthorId,id));

        if(delete > 0){
            return R.success("删除成功");
        }else return R.fail("删除失败");


    }
}
