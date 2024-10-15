package com.service.comments.dto;

import com.service.comments.models.Comment;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CommentResponse implements Serializable {

  private List<Comment> comments = new ArrayList<>();
  private int totalCount;
}
