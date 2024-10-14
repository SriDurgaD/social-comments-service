package com.service.comments.repository;

import com.service.comments.models.Reaction;
import com.service.comments.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query(value = "select u.* from Users u "
      + " inner join Reactions r on r.user_id = u.user_id where r.comment_id=:commentId and r.reaction_type=:reaction",
      nativeQuery = true)
  List<User> findUsersByReactionAndCommentId(@Param("reaction") String reaction,
      @Param("commentId") Long commentId);
}
