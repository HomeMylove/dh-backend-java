package com.homemylove.controller;

import com.homemylove.core.api.R;
import com.homemylove.entity.Comment;
import com.homemylove.service.CommentLikesService;
import com.homemylove.service.CommentService;
import com.homemylove.vo.CommentDetailVo;
import com.homemylove.vo.ListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private CommentLikesService commentLikesService;

    @GetMapping("/{articleId}/{order}")
    public R<ListVo<CommentDetailVo>> getComments(@PathVariable("articleId") Long articleId,
                                                  @PathVariable("order") String order){
        return R.data(commentService.getCommentList(articleId,order));
    }

    @PostMapping("/save")
    public R<String> saveComment(@RequestBody Comment comment){
        return commentService.saveComment(comment);
    }

    @GetMapping("/like/{commentId}")
    public R<String> likeComment(@PathVariable("commentId") Long commentId){
        return commentLikesService.likeComment(commentId);
    }

    @DeleteMapping("/{id}")
    public R<String> deleteComment(@PathVariable("id") Long id){
        return commentService.deleteComment(id);
    }

}
