package com.service.comments.utils;

import com.service.comments.exception.custom.CommentNotFoundException;
import com.service.comments.exception.custom.InvalidReactionTypeException;
import com.service.comments.models.Comment;
import com.service.comments.models.enums.ReactType;
import com.service.comments.repository.CommentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentServiceValidator {

  @Autowired
  CommentRepository commentRepository;

  public void validateCommentId(Long commentId) {
    List<Comment> commentDetails = commentRepository.findByParentCommentId(commentId);
    if (commentDetails == null || commentDetails.isEmpty()) {
      throw new CommentNotFoundException(
          String.format("Comment with commentId %s is not present.", commentId));
    }
  }

  public void validateReactionType(String reactType) {
    boolean isValid = false;
    for (ReactType value : ReactType.values()) {
      if (value.name().equals(reactType.toUpperCase())) {
        isValid = true;
        break;
      }
    }
    if (!isValid) {
      throw new InvalidReactionTypeException(
          String.format("ReactType %s passed is invalid.", reactType));
    }
  }

}
