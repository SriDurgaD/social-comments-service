package com.service.comments.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenerationTime;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "Comments")
public class Comment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;
  private String commentDesc;
  private Long postId;
  @Column(insertable = false, updatable = false)
  private Timestamp commentedTime;
  private Long parentCommentId;

  @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> replies = new ArrayList<>();

  @OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Reaction> reactions = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
