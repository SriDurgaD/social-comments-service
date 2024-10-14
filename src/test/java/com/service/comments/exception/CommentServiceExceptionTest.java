package com.service.comments.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentServiceExceptionTest {

  @Test
  public void testCommentServiceException_ShouldReturnCorrectMessage() {
    String errorMessage = "This is a custom error message.";

    CommentServiceException exception = assertThrows(CommentServiceException.class, () -> {
      throw new CommentServiceException(errorMessage);
    });

    assertEquals(errorMessage, exception.getMessage());

  }
}
