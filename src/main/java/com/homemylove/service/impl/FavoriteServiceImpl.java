package com.homemylove.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.homemylove.auth.AuthInfo;
import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.core.mdc.MDCScope;
import com.homemylove.core.mp.BaseServiceImpl;
import com.homemylove.entity.Article;
import com.homemylove.entity.FavItem;
import com.homemylove.entity.FavList;
import com.homemylove.mapper.ArticleMapper;
import com.homemylove.mapper.FavItemMapper;
import com.homemylove.mapper.FavListMapper;
import com.homemylove.service.FavoriteService;
import com.homemylove.utils.CollectionUtil;
import com.homemylove.vo.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private FavListMapper favListMapper;

    private FavItemMapper favItemMapper;

    private ArticleMapper articleMapper;

    @Override
    public R<String> editFavList(FavList favList) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后添加");
        // 判断 name 存在
        FavList one = favListMapper.selectOne(FavList.gw().eq(FavList::getName, favList.getName()).select(FavList::getId));
        if (one != null) return R.fail("收藏夹名重复");

        // 添加
        if (favList.getId() == null) {
            favList.setCreateTime(new Date());
            favList.setCreateUser(authInfo.getId());
            favListMapper.insert(favList);
        } else {
            favListMapper.updateById(favList);
        }
        return R.success("成功");
    }

    @Override
    public List<FavListVo> getFavList(Map<String, Object> data) {
        Object o = data.get("spaceId");
        if(o == null) return null;

        Long spaceId = Long.valueOf(o.toString());
        Object o1 = data.get("articleId");
        Long articleId = o1 == null ? null : Long.valueOf(o1.toString());

        List<FavList> favLists = favListMapper.selectList(FavList.gw().select(FavList::getId, FavList::getName,FavList::getType)
                .eq(FavList::getCreateUser, spaceId));

        // 如果没有，则新建一个默认的
        if(favLists.isEmpty()){
            FavList favList = new FavList();
            favList.setCreateUser(spaceId);
            favList.setName("我喜欢");
            favList.setCreateTime(new Date());
            favList.setType("default");
            favListMapper.insert(favList);
            favLists = List.of(favList);
        }

        return favLists.stream().map(f -> {
            // 获取条数
            Integer count = favItemMapper.selectCount(FavItem.gw().eq(FavItem::getFavId, f.getId()));
            FavListVo listVo = new FavListVo(f.getId(), f.getName(), f.getType(),count, 1000);
            if(articleId == null) return listVo;

            FavArticleListVo articleListVo = new FavArticleListVo();
            BeanUtils.copyProperties(listVo,articleListVo);
            // 获取是否已收藏
            FavItem favItem = favItemMapper.selectOne(FavItem.gw().select().eq(FavItem::getFavId, f.getId())
                    .eq(FavItem::getArticleId, articleId));
            articleListVo.setChecked(favItem != null);
            return articleListVo;
        }).toList();
    }

    @Override
    public R<String> deleteFavList(Long id) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后操作");

        int delete = favListMapper.delete(FavList.gw()
                .eq(FavList::getId, id)
                .eq(FavList::getCreateUser, authInfo.getId()));

        if (delete > 0) return R.success("成功");
        return R.fail("失败");

    }

    @Override
    public R<String> addArticleToFavList(Map<String, Object> data) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后操作");

        Long articleId = Long.valueOf(data.get("articleId").toString());
        List<Long> favList = (List<Long>) data.get("favList");

        // 删除相关 article 的所有收藏
        favItemMapper.delete(FavItem.gw()
                .eq(FavItem::getArticleId, articleId)
                .eq(FavItem::getCreateUser, authInfo.getId()));

        // 添加到 fav_item
        CollectionUtil.transformToList(favList).forEach(f -> {
            FavItem favItem = new FavItem();
            favItem.setArticleId(articleId);
            favItem.setCreateUser(authInfo.getId());
            favItem.setCreateTime(new Date());
            favItem.setFavId(f);
            favItemMapper.insert(favItem);
        });

        return R.success("成功");
    }

    @Override
    public ListVo<ArticleFavVo> getFavArticles(Long id, Query query) {
        PageHelper.startPage(query.getPageNum(),query.getPageSize());

        // 获取所有文章的 id
        List<FavItem> favItems = favItemMapper.selectList(FavItem.gw().select(FavItem::getArticleId,FavItem::getCreateTime,FavItem::getId)
                .eq(FavItem::getFavId, id));

        PageInfo<FavItem> pageInfo = new PageInfo<>(favItems);

        List<ArticleFavVo> searchVos = favItems.stream().map(f -> {
            ArticleFavVo articleFavVo = new ArticleFavVo();
            ArticleSearchVo articleSearchVo = articleMapper.selectArticleInfoById(f.getArticleId());
            // 如果是空的，获取信息
            BeanUtils.copyProperties(articleSearchVo,articleFavVo);
            articleFavVo.setCreateTime(f.getCreateTime());
            articleFavVo.setFavId(f.getId());
            return articleFavVo;
        }).toList();
        return new ListVo<>(searchVos,pageInfo.getTotal());
    }

    @Override
    public R<String> deleteFavArticle(Long id) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后操作");

        int delete = favItemMapper.delete(FavItem.gw().eq(FavItem::getId, id)
                .eq(FavItem::getCreateUser, authInfo.getId()));

        if(delete > 0) return R.success("删除成功");
        return R.fail("删除失败");
    }
}
