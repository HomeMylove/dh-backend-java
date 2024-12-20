package com.homemylove.service.impl;

import com.homemylove.auth.AuthInfo;
import com.homemylove.core.api.R;
import com.homemylove.core.mdc.MDCScope;
import com.homemylove.entity.CommentLikes;
import com.homemylove.mapper.CommentLikesMapper;
import com.homemylove.service.CommentLikesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CommentLikesServiceImpl implements CommentLikesService {

    @Resource
    private CommentLikesMapper commentLikesMapper;
    @Override
    public List<CommentLikes> getLikesByCommentId(Long commentId) {
        return commentLikesMapper.
                selectList(CommentLikes.gw().select(CommentLikes::getUserId).eq(CommentLikes::getCommentId, commentId));
    }

    @Override
    public R<String> likeComment(Long commentId) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if(authInfo == null) return R.fail("登陆后可点赞");

        // 查询有没有
        CommentLikes one = commentLikesMapper.selectOne(CommentLikes.gw().select(CommentLikes::getId)
                .eq(CommentLikes::getCommentId, commentId)
                .eq(CommentLikes::getUserId, authInfo.getId()));

        int infect = 0;

        if(one == null){
            // 没赞过
            CommentLikes commentLikes = new CommentLikes();
            commentLikes.setCommentId(commentId);
            commentLikes.setUserId(authInfo.getId());
            commentLikes.setCreateTime(new Date());
            infect = commentLikesMapper.insert(commentLikes);
        }else {
            // 删除
           infect =  commentLikesMapper.deleteById(one.getId());
        }

        if(infect == 1) return R.success("点赞成功");
        return R.fail("点赞失败");
    }
}
