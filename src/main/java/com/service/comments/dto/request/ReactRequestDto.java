package com.service.comments.dto.request;

import com.service.comments.models.enums.ReactType;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReactRequestDto implements Serializable {

  private Long userId;
  private String reactType;
}
