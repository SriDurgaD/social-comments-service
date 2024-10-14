package com.service.comments.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericExceptionHandlerTest {

  private final GenericExceptionHandler exceptionHandler = new GenericExceptionHandler();

  @Test
  public void testHandleCommentServiceException() {
    CommentServiceException exception = new CommentServiceException("Comment service error");
    ResponseEntity<String> response = exceptionHandler.handleCommentServiceException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Comment service error", response.getBody());
  }

  @Test
  public void testHandleGenericException() {
    Exception exception = new Exception("Generic error");
    ResponseEntity<String> response = exceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Generic error", response.getBody());
  }
}
