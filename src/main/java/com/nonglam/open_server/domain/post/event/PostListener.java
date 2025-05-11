package com.nonglam.open_server.domain.post.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nonglam.open_server.domain.comment.event.CommentCreatedEvent;
import com.nonglam.open_server.domain.post.PostRepository;
import com.nonglam.open_server.domain.post.dto.response.SentimentPredictionResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
public class PostListener {
  PostRepository postRepository;
  RestTemplate restTemplate;

  @Async
  @EventListener
  public void handleCreatePost(PostCreateEvent event) {
    String API_URL = "http://localhost:9999/predict";
    var post = event.post();
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String[] texts = { post.getContent() };
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("texts", texts);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
    var response = restTemplate.postForEntity(API_URL, request,
        SentimentPredictionResponse.class);
    var sentiment = response.getBody().predictions().getFirst();
    post.setSentiment(sentiment);
    postRepository.save(post);
  }

  @Async
  @EventListener
  public void handleUpdateCommentCount(CommentCreatedEvent event) {
    postRepository.incrementCommentCount(event.postId());
  }

  @Async
  @EventListener
  public void handleViewPost(ViewPostEvent event) {
    postRepository.incrementViewCount(event.id());
  }
}
