package com.service.comments.exception.custom;

import com.service.comments.exception.CommentServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvalidReactionTypeExceptionTest {

  @Test
  public void testInvalidReactionTypeException_ShouldReturnCorrectMessage() {
    String errorMessage = "Invalid reaction type.";

    InvalidReactionTypeException exception =
        assertThrows(InvalidReactionTypeException.class, () -> {
          throw new InvalidReactionTypeException(errorMessage);
        });

    assertEquals(errorMessage, exception.getMessage());
    assertEquals(CommentServiceException.class, exception.getClass().getSuperclass());
  }
}
