package com.service.comments.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;
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

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(insertable = false, updatable = false)
  private int repliesCount;
  @Column(insertable = false, updatable = false)
  private int likesCount;
  @Column(insertable = false, updatable = false)
  private int dislikesCount;

}
