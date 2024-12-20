package com.homemylove.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homemylove.entity.Article;
import com.homemylove.vo.ArticleCreateVo;
import com.homemylove.vo.ArticleSearchVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT a.id id,a.create_time create_time, a.title title, au.id author_id, au.kanji_name author_name,u.id user_id,u.username user_name " +
            "FROM article a " +
            "LEFT JOIN author au ON a.author_id = au.id " +
            "LEFT JOIN t_user u ON a.create_user = u.id " +
            "WHERE a.create_user is not null  AND a.deleted = 0 ORDER BY a.id DESC LIMIT #{i}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "author.id", column = "author_id"),
            @Result(property = "author.name", column = "author_name"),
            @Result(property = "creator.id", column = "user_id"),
            @Result(property = "creator.name", column = "user_name")
    })
    List<ArticleCreateVo> getLatestArticle(int i);


    @Select("SELECT a.id id,a.create_time create_time, a.title title, au.id author_id, au.kanji_name author_name,u.id user_id,u.username user_name " +
            "FROM article a " +
            "LEFT JOIN author au ON a.author_id = au.id " +
            "LEFT JOIN t_user u ON a.create_user = u.id " +
            "WHERE a.title LIKE CONCAT('%',#{keyword},'%') AND a.deleted = 0")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "author.id", column = "author_id"),
            @Result(property = "author.name", column = "author_name"),
            @Result(property = "creator.id", column = "user_id"),
            @Result(property = "creator.name", column = "user_name")
    })
    List<ArticleSearchVo> selectArticleList(@Param("keyword") String keyword);

    @Select("SELECT a.id id,a.create_time create_time,a.deleted deleted, a.title title, au.id author_id, au.kanji_name author_name,u.id user_id,u.username user_name " +
            "FROM article a " +
            "LEFT JOIN author au ON a.author_id = au.id " +
            "LEFT JOIN t_user u ON a.create_user = u.id " +
            "WHERE a.id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "deleted", column = "deleted"),
            @Result(property = "author.id", column = "author_id"),
            @Result(property = "author.name", column = "author_name"),
            @Result(property = "creator.id", column = "user_id"),
            @Result(property = "creator.name", column = "user_name")
    })
    ArticleSearchVo selectArticleInfoById(@Param("id") Long id);
}
