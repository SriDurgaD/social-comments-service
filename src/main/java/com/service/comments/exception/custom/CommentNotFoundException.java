package com.service.comments.exception.custom;

import com.service.comments.exception.CommentServiceException;

public class CommentNotFoundException extends CommentServiceException {
  public CommentNotFoundException(String message) {
    super(message);
  }
}
