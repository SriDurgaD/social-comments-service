package com.service.comments.dto.request;


import lombok.Data;

import java.io.Serializable;

@Data
public class CommentRequestDto implements Serializable {
  private Long userId;
  private Long postId;
  private Long parentCommentId;
  private String comment;
}
