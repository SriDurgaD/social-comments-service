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
import com.service.comments.models.enums.ReactType;
import com.service.comments.repository.CommentRepository;
import com.service.comments.repository.ReactRepository;
import com.service.comments.repository.UserRepository;
import com.service.comments.utils.CommentServiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentsServiceImplTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private ReactRepository reactRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CommentServiceValidator commentServiceValidator;

  @InjectMocks
  private CommentsServiceImpl commentsService;

  private CommentRequestDto commentRequestDto;
  private Comment comment;
  private ReactRequestDto reactRequestDto;
  private User user;

  @BeforeEach
  void setUp() {
    commentRequestDto = new CommentRequestDto();
    commentRequestDto.setPostId(1L);
    commentRequestDto.setComment("This is a test comment");
    commentRequestDto.setUserId(1L);
    commentRequestDto.setParentCommentId(null);

    comment = new Comment();
    comment.setCommentId(1L);
    comment.setCommentDesc("This is a test comment");

    reactRequestDto = new ReactRequestDto();
    reactRequestDto.setReactType("LIKE");
    reactRequestDto.setUserId(1L);

    user = new User();
    user.setUserId(1L);
  }

  @Test
  void testAddComment() throws CommentNotFoundException {
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    EntityResponseDto response = commentsService.addComment(commentRequestDto);

    assertNotNull(response);
    assertEquals(1L, response.getCommentId());
    verify(commentServiceValidator, never()).validateCommentId(anyLong());
    verify(commentRepository, times(1)).save(any(Comment.class));
  }

  @Test
  void testAddCommentWithParentId() throws CommentNotFoundException {
    commentRequestDto.setParentCommentId(2L);
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    EntityResponseDto response = commentsService.addComment(commentRequestDto);

    assertNotNull(response);
    assertEquals(1L, response.getCommentId());
    verify(commentServiceValidator, times(1)).validateCommentId(2L);
    verify(commentRepository, times(1)).save(any(Comment.class));
  }

  @Test
  void testAddReact() throws CommentNotFoundException, InvalidReactionTypeException {
    doNothing().when(commentServiceValidator).validateReactionType(anyString());
    doNothing().when(commentServiceValidator).validateCommentId(anyLong());

    commentsService.addReact(1L, reactRequestDto);

    verify(commentServiceValidator, times(1)).validateReactionType("LIKE");
    verify(commentServiceValidator, times(1)).validateCommentId(1L);
    verify(reactRepository, times(1)).save(any(Reaction.class));
  }

  @Test
  void testGetCommentsWithoutParentId() throws CommentNotFoundException {
    Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment));
    when(commentRepository.findByParentCommentIdIsNull(any(Pageable.class)))
        .thenReturn(commentPage);

    CommentResponse response = commentsService.getComments(null, 0, 10);

    assertNotNull(response);
    assertEquals(1, response.getComments().size());
    assertEquals(1, response.getTotalCount());
    verify(commentRepository, times(1)).findByParentCommentIdIsNull(any(Pageable.class));
  }

  @Test
  void testGetCommentsWithParentId() throws CommentNotFoundException {
    Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment));
    when(commentRepository.findByParentCommentId(anyLong(), any(Pageable.class)))
        .thenReturn(commentPage);

    CommentResponse response = commentsService.getComments(2L, 0, 10);

    assertNotNull(response);
    assertEquals(1, response.getComments().size());
    assertEquals(1, response.getTotalCount());
    verify(commentServiceValidator, times(1)).validateCommentId(2L);
    verify(commentRepository, times(1)).findByParentCommentId(anyLong(), any(Pageable.class));
  }

  @Test
  void testGetUsersWrtReactType() {
    when(userRepository.findUsersByReactionAndCommentId(anyString(), anyLong()))
        .thenReturn(Collections.singletonList(user));

    List<User> users = commentsService.getUsersWrtReactType(1L, "LIKE");

    assertNotNull(users);
    assertEquals(1, users.size());
    assertEquals(1L, users.get(0).getUserId());
    verify(commentServiceValidator, times(1)).validateReactionType("LIKE");
    verify(userRepository, times(1)).findUsersByReactionAndCommentId("LIKE", 1L);


  }
}
