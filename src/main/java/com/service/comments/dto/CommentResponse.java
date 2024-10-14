package com.service.comments.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class CommentResponse implements Serializable {
  private Long commentId;
  private String commentDesc;
  private Timestamp commentedTime;
  // private Date updatedAt;
  // private String userId;
  // private long commentsCount;
  // private long likesCount;
  // private long dislikesCount;
  // private String postId;
  // private String parentId;
}
