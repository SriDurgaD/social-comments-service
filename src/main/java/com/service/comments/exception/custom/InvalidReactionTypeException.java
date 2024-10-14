package com.service.comments.exception.custom;

import com.service.comments.exception.CommentServiceException;

public class InvalidReactionTypeException extends CommentServiceException {
  public InvalidReactionTypeException(String message) {
    super(message);
  }

}
