package com.service.comments.repository;

import com.service.comments.models.Comment;
import jakarta.persistence.criteria.From;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query(value = "select k.comment_id," + "k.replies_count," + "k.comment_desc," + "k.user_id, "
      + "k.commented_time," + "k.post_id," + "k.parent_comment_id, "
      + "sum(case when reaction_type='LIKE' then 1 else 0 end) as likes_count, "
      + "sum(case when reaction_type='DISLIKE' then 1 else 0 end) as dislikes_count from ( Select Parent.*, "
      + "Count(Child.comment_id) As replies_count "
      + "From comments As Parent Left Join comments As Child On Parent.comment_id = Child.parent_comment_id "
      + "where Parent.parent_comment_id is NULL " + "GROUP by comment_id "
      + ") k left join Reactions r on  k.comment_id = r.comment_id " + "group by comment_id",
      countQuery = """
          select count(p.comment_id)
          from Comments p
          where p.comment_id is NULL
          """, nativeQuery = true)
  Page<Comment> findByParentCommentIdIsNull(Pageable pageable);

  @Query(value = "select k.comment_id," + "k.replies_count," + "k.comment_desc," + "k.user_id, "
      + "k.commented_time," + "k.post_id," + "k.parent_comment_id, "
      + "sum(case when reaction_type='LIKE' then 1 else 0 end) as likes_count, "
      + "sum(case when reaction_type='DISLIKE' then 1 else 0 end) as dislikes_count from ( Select Parent.*, "
      + "Count(Child.comment_id) As replies_count "
      + "From comments As Parent Left Join comments As Child On Parent.comment_id = Child.parent_comment_id "
      + "where Parent.parent_comment_id=:parentCommentId " + "GROUP by comment_id "
      + ") k left join Reactions r on  k.comment_id = r.comment_id " + "group by comment_id",
      countQuery = """
          select count(p.comment_id)
          from Comments p
          where p.comment_id=:parentCommentId
          """, nativeQuery = true)
  Page<Comment> findByParentCommentId(Long parentCommentId, Pageable pageable);

  @Query(
      value = """
          select
          	k.comment_id,
          	k.replies_count,
          	k.comment_desc,
          	k.user_id,
          	k.commented_time,
          	k.post_id,
          	k.parent_comment_id,
          	sum(case when reaction_type = 'LIKE' then 1 else 0 end) as likes_count,
          	sum(case when reaction_type = 'DISLIKE' then 1 else 0 end) as dislikes_count
          from
          	(
          	Select
          		Parent.comment_id,Parent.comment_desc, Parent.user_id, Parent.commented_time, Parent.post_id, Parent.parent_comment_id,
          		Count(Child.comment_id) As replies_count
          	From
          		comments As Parent
          	Left Join comments As Child On
          		Parent.comment_id = Child.parent_comment_id
          	where
          		Parent.parent_comment_id =:parentCommentId
          	GROUP by
          		comment_id ) k
          left join Reactions r on
          	k.comment_id = r.comment_id
          group by
          	comment_id
          """,
      nativeQuery = true)
  List<Comment> findByParentCommentId(Long parentCommentId);

}
