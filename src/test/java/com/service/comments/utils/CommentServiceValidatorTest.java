package com.service.comments.utils;

import com.service.comments.exception.custom.CommentNotFoundException;
import com.service.comments.exception.custom.InvalidReactionTypeException;
import com.service.comments.models.Comment;
import com.service.comments.models.enums.ReactType;
import com.service.comments.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceValidatorTest {

  @Mock
  private CommentRepository commentRepository;

  @InjectMocks
  private CommentServiceValidator commentServiceValidator;

  private final Long VALID_COMMENT_ID = 1L;
  private final Long INVALID_COMMENT_ID = 99L;

//  @BeforeEach
//  public void setup() {
//    // Mocking the repository behavior for a valid comment ID
//    when(commentRepository.findByParentCommentId(VALID_COMMENT_ID))
//        .thenReturn(List.of(new Comment()));
//    // Mocking the repository behavior for an invalid comment ID
//    when(commentRepository.findByParentCommentId(INVALID_COMMENT_ID))
//        .thenReturn(Collections.emptyList());
//  }

  @Test
  public void testValidateCommentId_ShouldNotThrowException_WhenCommentExists() {
    when(commentRepository.findByParentCommentId(VALID_COMMENT_ID))
        .thenReturn(List.of(new Comment()));
    commentServiceValidator.validateCommentId(VALID_COMMENT_ID);
  }

  @Test
  public void testValidateCommentId_ShouldThrowCommentNotFoundException_WhenCommentDoesNotExist() {
    assertThrows(CommentNotFoundException.class, () -> {
      commentServiceValidator.validateCommentId(INVALID_COMMENT_ID);
    });
  }

  @Test
  public void testValidateReactionType_ShouldNotThrowException_WhenValidReactType() {
    // Test with a valid reaction type
    CommentServiceValidator.validateReactionType("LIKE");
  }

  @Test
  public void testValidateReactionType_ShouldThrowInvalidReactionTypeException_WhenInvalidReactType() {
    // Test with an invalid reaction type
    assertThrows(InvalidReactionTypeException.class, () -> {
      CommentServiceValidator.validateReactionType("INVALID_TYPE");
    });
  }

  @Test
  public void testValidateReactionType_ShouldBeCaseInsensitive_WhenValidReactType() {
    // Test with a valid reaction type in lowercase
    CommentServiceValidator.validateReactionType("like");
  }
}