package com.homemylove.service;

import com.homemylove.core.api.R;
import com.homemylove.entity.CommentLikes;

import java.util.List;

public interface CommentLikesService {

    /**
     * 根据评论 id 获取点赞人 id
     * @param commentId 评论 id
     * @return 谁点赞了
     */
    List<CommentLikes> getLikesByCommentId(Long commentId);

    R<String> likeComment(Long commentId);

}
