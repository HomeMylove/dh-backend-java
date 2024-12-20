package com.homemylove.service;


import com.homemylove.core.api.R;
import com.homemylove.entity.Comment;
import com.homemylove.vo.CommentDetailVo;
import com.homemylove.vo.ListVo;

public interface CommentService {

    ListVo<CommentDetailVo> getCommentList(Long articleId,String order);

    R<String> saveComment(Comment comment);

    R<String> deleteComment(Long id);
}
