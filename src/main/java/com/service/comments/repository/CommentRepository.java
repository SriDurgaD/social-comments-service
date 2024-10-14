package com.service.comments.repository;

import com.service.comments.models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByParentCommentIdIsNull(Pageable pageable);

  List<Comment> findByParentCommentId(Long parentCommentId, Pageable pageable);

  List<Comment> findByParentCommentId(Long parentCommentId);

}
