package com.service.comments.exception.custom;

import com.service.comments.exception.CommentServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentNotFoundExceptionTest {

  @Test
  public void testCommentNotFoundException_ShouldReturnCorrectMessage() {
    String errorMessage = "Comment not found.";

    CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> {
      throw new CommentNotFoundException(errorMessage);
    });

    assertEquals(errorMessage, exception.getMessage());
    assertEquals(CommentServiceException.class, exception.getClass().getSuperclass());
  }
}
