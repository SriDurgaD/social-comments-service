package com.service.comments.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

  @ExceptionHandler({CommentServiceException.class})
  public ResponseEntity<String> handleCommentServiceException(CommentServiceException e) {
    log.error("Application Exception occurred is {}", e, e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<String> handleException(Exception e) {
    log.error("Generic Exception occurred is {}", e, e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
