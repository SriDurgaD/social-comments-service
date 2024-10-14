package com.service.comments.models.enums;

public enum ReactType {
  LIKE("LIKE"), DISLIKE("DISLIKE");

  final String value;

  ReactType(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}
