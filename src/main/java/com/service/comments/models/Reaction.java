package com.service.comments.models;

import com.service.comments.models.enums.ReactType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "Reactions")
public class Reaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reactionId;
  private Long commentId;
  private Long userId;
  private String reactionType;
  @Column(insertable = false, updatable = false)
  private Timestamp reaction_time;


}
