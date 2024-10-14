package com.service.comments.repository;

import com.service.comments.models.Comment;
import com.service.comments.models.Reaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactRepository extends JpaRepository<Reaction, Long> {

}
