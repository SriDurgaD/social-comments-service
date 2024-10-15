package com.service.comments.service.impl;

import com.service.comments.dto.CommentResponse;
import com.service.comments.dto.request.CommentRequestDto;
import com.service.comments.dto.EntityResponseDto;
import com.service.comments.dto.request.ReactRequestDto;
import com.service.comments.exception.custom.CommentNotFoundException;
import com.service.comments.exception.custom.InvalidReactionTypeException;
import com.service.comments.models.Comment;
import com.service.comments.models.Reaction;
import com.service.comments.models.User;
import com.service.comments.repository.CommentRepository;
import com.service.comments.repository.ReactRepository;
import com.service.comments.repository.UserRepository;
import com.service.comments.service.CommentsService;
import com.service.comments.utils.CommentServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private ReactRepository reactRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CommentServiceValidator commentServiceValidator;

  public EntityResponseDto addComment(CommentRequestDto commentRequestDt)
      throws CommentNotFoundException {
    EntityResponseDto response = new EntityResponseDto();
    Comment comment = new Comment();
    comment.setPostId(commentRequestDt.getPostId());
    comment.setCommentDesc(commentRequestDt.getComment());
    comment.setUser(new User());
    comment.getUser().setUserId(commentRequestDt.getUserId());
    Long parentCommentId = commentRequestDt.getParentCommentId();
    if (parentCommentId != null) {
      commentServiceValidator.validateCommentId(parentCommentId);
      comment.setParentCommentId(parentCommentId);
    }
    response.setCommentId(commentRepository.save(comment).getCommentId());
    return response;
  }

  public void addReact(Long commentId, ReactRequestDto reactRequestDto)
      throws CommentNotFoundException, InvalidReactionTypeException {
    String reactType = reactRequestDto.getReactType();
    Reaction likeDislike = new Reaction();
    Long userId = reactRequestDto.getUserId();
    commentServiceValidator.validateReactionType(reactType);
    commentServiceValidator.validateCommentId(commentId);
    likeDislike.setReactionType(reactRequestDto.getReactType());
    likeDislike.setCommentId(commentId);
    likeDislike.setUserId(userId);
    Reaction userReaction = reactRepository.findByUserIdAndCommentId(userId, commentId);
    if (userReaction == null) {
      reactRepository.save(likeDislike);
    } else {
      userReaction.setReactionType(reactType);
      reactRepository.save(userReaction);
    }
  }

  public CommentResponse getComments(Long parentCommentId, int page, int size)
      throws CommentNotFoundException {
    Pageable pageable = PageRequest.of(page, size);
    CommentResponse response = new CommentResponse();
    Page<Comment> data;
    if (parentCommentId == null) {
      data = commentRepository.findByParentCommentIdIsNull(pageable);
    } else {
      commentServiceValidator.validateCommentId(parentCommentId);
      data = commentRepository.findByParentCommentId(parentCommentId, pageable);
    }
    response.setComments(data.getContent());
    response.setTotalCount(data.getTotalPages());
    return response;
  }

  public List<User> getUsersWrtReactType(Long commentId, String reactType) {
    commentServiceValidator.validateReactionType(reactType);
    return userRepository.findUsersByReactionAndCommentId(reactType, commentId);
  }
}
