package com.homemylove.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homemylove.auth.AuthInfo;
import com.homemylove.core.api.R;
import com.homemylove.core.cs.CS;
import com.homemylove.core.mdc.MDCScope;
import com.homemylove.entity.Comment;
import com.homemylove.entity.CommentLikes;
import com.homemylove.entity.User;
import com.homemylove.mapper.CommentMapper;
import com.homemylove.mapper.UserMapper;
import com.homemylove.service.CommentLikesService;
import com.homemylove.service.CommentService;
import com.homemylove.utils.DateUtil;
import com.homemylove.vo.CommentDetailVo;
import com.homemylove.vo.CommenterVo;
import com.homemylove.vo.ListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommentLikesService commentLikesService;

    @Override
    public ListVo<CommentDetailVo> getCommentList(Long articleId, String order) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        LambdaQueryWrapper<Comment> wrapper = Comment.gw().eq(Comment::getArticleId, articleId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        List<CommentDetailVo> vos = new ArrayList<>();
        // 进行处理
        AtomicInteger size = new AtomicInteger();
        comments.forEach(c -> {
            CommentDetailVo vo = new CommentDetailVo();
            BeanUtils.copyProperties(c, vo);
            // 格式化创建时间
            Date createTime = c.getCreateTime();
            Date now = new Date();
            if (DateUtil.lessThan(now, createTime, 2L, DateUtil.TimeUnits.MINUTE)) {
                vo.setCreateTime("刚刚");
            } else {
                vo.setCreateTime(DateUtil.formatDateTime(createTime));
            }

            // 获取评论人
            User one = userMapper.selectOne(User.gw().select(User::getUsername, User::getAvatar).eq(User::getId, c.getCommenterId()));
            CommenterVo commenterVo = new CommenterVo(c.getCommenterId(), one.getUsername(), one.getAvatar());
            vo.setCommenter(commenterVo);
            // 获取点赞数
            List<CommentLikes> likes = commentLikesService.getLikesByCommentId(c.getId());
            if (likes == null) {
                vo.setLikes(0L);
                vo.setLiked(false);
            } else {
                vo.setLikes((long) likes.size());
                if (authInfo == null) {
                    // 没登陆
                    vo.setLiked(false);
                } else {
                    vo.setLiked(likes.stream().anyMatch(v -> authInfo.getId().equals(v.getUserId())));
                }
            }

            // 没有 pid 说明不是回复的
            if (c.getParentId() == null) {
                // 查询出 commenter
                vos.add(vo);
                size.getAndIncrement();
            }
            // 有 pid 是回复
            else {
                // 找 p 是谁
                for (CommentDetailVo commentDetailVo : vos) {
                    // 如果 pid == vo.id 那么就是第一层的回复
                    List<CommentDetailVo> children = commentDetailVo.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                    }

                    if (c.getParentId().equals(commentDetailVo.getId())) {
                        children.add(vo);
                        commentDetailVo.setChildren(children);
                        size.getAndIncrement();
                        break;
                    } else {
                        for (CommentDetailVo child : children) {
                            // 如果和二级评论一样
                            if (c.getParentId().equals(child.getId())) {
                                vo.setFather(child.getCommenter());
                                children.add(vo);
                                size.getAndIncrement();
                                break;
                            }
                        }
                    }
                }
            }
        });

        // 排序
        // 按照热度排序
        if (CS.SORTED_WAY.BY_LIKES.equals(order)) {
            vos.sort((v1, v2) -> Math.toIntExact(v2.getLikes() - v1.getLikes()));
        } else {
            Collections.reverse(vos);
        }
        ListVo<CommentDetailVo> listVo = new ListVo<>();
        listVo.setData(vos);

        listVo.setSize(size.get());


        return listVo;
    }

    @Override
    public R<String> saveComment(Comment comment) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) {
            return R.fail("登陆后评论");

        }
        comment.setCommenterId(authInfo.getId());
        comment.setCreateTime(new Date());
        int insert = commentMapper.insert(comment);
        if (insert == 1) {
            return R.success("评论成功");
        } else {
            return R.fail("评论失败");
        }
    }

    @Override
    public R<String> deleteComment(Long id) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return R.fail("登陆后删除");
        int delete = commentMapper.delete(Comment.gw().eq(Comment::getId, id).eq(Comment::getCommenterId, authInfo.getId()));
        if (delete == 1) {
            // 删除所有引用了我的
            commentMapper.delete(Comment.gw().eq(Comment::getParentId,id));
            return R.success("删除成功");
        }
        return R.fail("删除失败");
    }
}
