package com.homemylove.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homemylove.entity.ReadHistory;
import com.homemylove.vo.ReadHistoryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReadHistoryMapper extends BaseMapper<ReadHistory> {

    @Select("SELECT h.id AS id,h.page page,h.total total,h.create_time create_time, a.id AS article_id, a.title AS title, au.id AS author_id, au.kanji_name AS name " +
            "FROM read_history h " +
            "LEFT JOIN article a ON h.article_id = a.id " +
            "LEFT JOIN author au ON a.author_id = au.id " +
            "WHERE h.user_id = #{userId} AND a.title  LIKE CONCAT('%', #{title}, '%') AND h.deleted = 0 order by h.id desc")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "page", column = "page"),
            @Result(property = "total", column = "total"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "article.id", column = "article_id"),
            @Result(property = "article.title", column = "title"),
            @Result(property = "author.id", column = "author_id"),
            @Result(property = "author.name", column = "name")
    })
    List<ReadHistoryVo> getReadHistoryVoByUserId(@Param("userId") Long userId, @Param("title") String title);


}
