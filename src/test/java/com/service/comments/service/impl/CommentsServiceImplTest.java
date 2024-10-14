package com.service.comments.service.impl;

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
import com.service.comments.utils.CommentServiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
  private ReactRequestDto reactRequestDto;
  private Comment comment;

  @BeforeEach
  public void setup() {
    commentRequestDto = new CommentRequestDto();
    commentRequestDto.setPostId(1L);
    commentRequestDto.setComment("Test comment");
    commentRequestDto.setUserId(1L);
    commentRequestDto.setParentCommentId(1L);

    reactRequestDto = new ReactRequestDto();
    reactRequestDto.setReactType("LIKE");
    reactRequestDto.setUserId(1L);

    comment = new Comment();
    comment.setCommentId(1L);
    comment.setCommentDesc("Test comment");
  }

  @Test
  public void testAddComment_ShouldReturnEntityResponseDto_WhenCommentIsAdded() {
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    EntityResponseDto response = commentsService.addComment(commentRequestDto);

    assertEquals(comment.getCommentId(), response.getCommentId());
    verify(commentServiceValidator, times(1)).validateCommentId(
        commentRequestDto.getParentCommentId());
    verify(commentRepository, times(1)).save(any(Comment.class));
  }

  @Test
  public void testAddReact_ShouldThrowCommentNotFoundException_WhenCommentIdIsInvalid() {
    doThrow(CommentNotFoundException.class).when(commentServiceValidator).validateCommentId(2L);

    assertThrows(CommentNotFoundException.class, () -> {
      commentsService.addReact(2L, reactRequestDto);
    });

    verify(commentServiceValidator, times(1)).validateCommentId(2L);
  }

  @Test
  public void testAddReact_ShouldThrowInvalidReactionTypeException_WhenReactTypeIsInvalid() {
    reactRequestDto.setReactType("INVALID");

    doThrow(InvalidReactionTypeException.class).when(commentServiceValidator)
        .validateReactionType("INVALID");

    assertThrows(InvalidReactionTypeException.class, () -> {
      commentsService.addReact(1L, reactRequestDto);
    });

    verify(commentServiceValidator, times(1)).validateReactionType("INVALID");
  }

  @Test
  public void testGetRepliesForComments_ShouldReturnListOfComments_WhenParentCommentIdIsValid() {
    when(commentRepository.findByParentCommentId(1L, PageRequest.of(0, 5)))
        .thenReturn(List.of(comment));

    List<Comment> replies = commentsService.getRepliesForComments(1L, 0, 5);

    assertEquals(1, replies.size());
    assertEquals(comment.getCommentDesc(), replies.get(0).getCommentDesc());
    verify(commentServiceValidator, times(1)).validateCommentId(1L);
    verify(commentRepository, times(1)).findByParentCommentId(1L, PageRequest.of(0, 5));
  }

  @Test
  public void testGetRepliesForComments_ShouldThrowCommentNotFoundException_WhenParentCommentIdIsInvalid() {
    doThrow(CommentNotFoundException.class).when(commentServiceValidator).validateCommentId(2L);

    assertThrows(CommentNotFoundException.class, () -> {
      commentsService.getRepliesForComments(2L, 0, 5);
    });

    verify(commentServiceValidator, times(1)).validateCommentId(2L);
  }

  @Test
  public void testGetUsersWrtReactType_ShouldReturnListOfUsers_WhenReactTypeIsValid() {
    User user = new User();
    user.setUserId(1L);
    when(userRepository.findUsersByReactionAndCommentId("LIKE", 1L)).thenReturn(List.of(user));

    List<User> users = commentsService.getUsersWrtReactType(1L, "LIKE");

    assertEquals(1, users.size());
    assertEquals(1L, users.get(0).getUserId());
    verify(commentServiceValidator, times(1)).validateReactionType("LIKE");
    verify(userRepository, times(1)).findUsersByReactionAndCommentId("LIKE", 1L);
  }

  @Test
  public void testGetUsersWrtReactType_ShouldThrowInvalidReactionTypeException_WhenReactTypeIsInvalid() {
    doThrow(InvalidReactionTypeException.class).when(commentServiceValidator)
        .validateReactionType("INVALID");

    assertThrows(InvalidReactionTypeException.class, () -> {
      commentsService.getUsersWrtReactType(1L, "INVALID");
    });

    verify(commentServiceValidator, times(1)).validateReactionType("INVALID");
  }
}