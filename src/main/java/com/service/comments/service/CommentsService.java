package com.service.comments.service;

import com.service.comments.dto.CommentResponse;
import com.service.comments.dto.request.CommentRequestDto;
import com.service.comments.dto.EntityResponseDto;
import com.service.comments.dto.request.ReactRequestDto;
import com.service.comments.exception.custom.CommentNotFoundException;
import com.service.comments.models.Comment;
import com.service.comments.models.User;

import java.util.List;

public interface CommentsService {

  public EntityResponseDto addComment(CommentRequestDto commentRequestDto)
      throws CommentNotFoundException;

  public void addReact(Long commentId, ReactRequestDto reactRequestDto);

  public CommentResponse getComments(Long parentCommentId, int page, int size);

  public List<User> getUsersWrtReactType(Long commentId, String reactType);

}
