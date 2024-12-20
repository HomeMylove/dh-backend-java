package com.homemylove.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommentDetailVo {
    private Long id;
    private CommenterVo commenter;
    private String commentText;
    private Long likes;
    private boolean liked; // 是否已经点过赞了
    private String createTime;
    private CommenterVo father;
    private List<CommentDetailVo> children;
}
