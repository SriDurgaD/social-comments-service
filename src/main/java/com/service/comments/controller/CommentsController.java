package com.service.comments.controller;

import com.service.comments.dto.CommentResponse;
import com.service.comments.dto.request.CommentRequestDto;
import com.service.comments.dto.EntityResponseDto;
import com.service.comments.dto.request.ReactRequestDto;
import com.service.comments.dto.ResponseDto;
import com.service.comments.models.Comment;
import com.service.comments.models.User;
import com.service.comments.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentsController {

  @Autowired
  private CommentsService commentsService;

  @PostMapping
  public ResponseDto<EntityResponseDto> comment(@RequestBody CommentRequestDto commentRequestDto) {
    return new ResponseDto<>(commentsService.addComment(commentRequestDto));
  }

  @PutMapping("/{commentId}/react")
  public ResponseDto<String> react(@PathVariable Long commentId,
      @RequestBody ReactRequestDto reactRequestDto) {
    commentsService.addReact(commentId, reactRequestDto);
    return new ResponseDto<>("Reacted to Comment");
  }

  @GetMapping()
  public ResponseDto<CommentResponse> getComments(
      @RequestParam(required = false) Long parentCommentId,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int size,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int page) {
    return new ResponseDto<>(commentsService.getComments(parentCommentId, page, size));
  }

  @GetMapping("/{commentId}/react/{reactType}/users")
  public ResponseDto<List<User>> getUsersWrtReactType(@PathVariable Long commentId,
      @PathVariable String reactType) {
    return new ResponseDto<>(commentsService.getUsersWrtReactType(commentId, reactType));
  }

}
