package com.service.comments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.comments.dto.request.CommentRequestDto;
import com.service.comments.dto.EntityResponseDto;
import com.service.comments.dto.request.ReactRequestDto;
import com.service.comments.dto.ResponseDto;
import com.service.comments.models.Comment;
import com.service.comments.models.User;
import com.service.comments.service.CommentsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentsController.class)
public class CommentsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CommentsService commentsService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testComment_ShouldReturnEntityResponseDto_WhenCommentIsAdded() throws Exception {
    CommentRequestDto commentRequestDto = new CommentRequestDto();
    commentRequestDto.setPostId(1L);
    commentRequestDto.setComment("Test comment");
    commentRequestDto.setUserId(1L);

    EntityResponseDto responseDto = new EntityResponseDto();
    responseDto.setCommentId(1L);

    when(commentsService.addComment(any(CommentRequestDto.class))).thenReturn(responseDto);

    mockMvc
        .perform(post("/api/v1/comments").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequestDto)))
        .andExpect(status().isOk())

        .andExpect(content().json(objectMapper.writeValueAsString(new ResponseDto<>(responseDto))));
  }

  @Test
  public void testReactOnPost_ShouldReturnSuccessMessage_WhenReactionIsAdded() throws Exception {
    ReactRequestDto reactRequestDto = new ReactRequestDto();
    reactRequestDto.setReactType("LIKE");
    reactRequestDto.setUserId(1L);

    Mockito.doNothing().when(commentsService).addReact(eq(1L), any(ReactRequestDto.class));

    mockMvc
        .perform(put("/api/v1/comments/1/react").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reactRequestDto)))
        .andExpect(status().isOk())

        .andExpect(content()
            .json(objectMapper.writeValueAsString(new ResponseDto<>("Reacted to Comment"))));
  }

  @Test
  public void testGetRepliesForComments_ShouldReturnListOfComments_WhenCalled() throws Exception {
    Comment comment = new Comment();
    comment.setCommentId(1L);
    comment.setCommentDesc("Test reply");

    when(commentsService.getRepliesForComments(null, 0, 10)).thenReturn(List.of(comment));

    mockMvc.perform(get("/api/v1/comments").param("pageNo", "0").param("pageSize", "10"))
        .andExpect(status().isOk())

        .andExpect(
            content().json(objectMapper.writeValueAsString(new ResponseDto<>(List.of(comment)))));
  }

  @Test
  public void testGetUsersWrtReactType_ShouldReturnListOfUsers_WhenCalled() throws Exception {
    User user = new User();
    user.setUserId(1L);

    when(commentsService.getUsersWrtReactType(1L, "LIKE")).thenReturn(List.of(user));

    mockMvc.perform(get("/api/v1/comments/1/react/LIKE/users")).andExpect(status().isOk())

        .andExpect(
            content().json(objectMapper.writeValueAsString(new ResponseDto<>(List.of(user)))));


  }
}
