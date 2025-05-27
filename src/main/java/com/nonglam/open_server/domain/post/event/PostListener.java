package com.nonglam.open_server.domain.post.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.nonglam.open_server.domain.comment.event.CommentCreatedEvent;
import com.nonglam.open_server.domain.post.Post;
import com.nonglam.open_server.domain.post.PostRepository;
import com.nonglam.open_server.domain.post.dto.response.SentimentPredictionResponse;
import com.nonglam.open_server.domain.post.dto.response.SpamPredictioResponse;
import com.nonglam.open_server.exception.ApiException;
import com.nonglam.open_server.shared.ErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
@Log4j2
public class PostListener {
  PostRepository postRepository;
  RestTemplate restTemplate;
  String SENTIMENT_PREDICT_ENDPOINT = "http://localhost:9999/predict";
  String SPAM_PREDICT_ENDPOINT = "http://localhost:9999/spam-predict";

  private HttpEntity<Map<String, Object>> prepareRequest(String content) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String[] texts = { content };
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("texts", texts);
    return new HttpEntity<>(requestBody, headers);
  }

  @EventListener
  @Transactional
  public void handleCreatePost(PostCreateEvent event) {
    var post = event.post();
    var request = prepareRequest(post.getContent());
    int spam = -1;
    int sentiment = predictSentiment(request, post);
    try {
      var spamResponse = restTemplate.postForEntity(SPAM_PREDICT_ENDPOINT, request,
          SpamPredictioResponse.class);
      spam = spamResponse.getBody().predictions().getFirst();
    } catch (ResourceAccessException exception) {
      log.warn("Spam prediction service is not available");
    }
    if (spam == 1) {
      throw new ApiException("Post content spam detected", ErrorCode.POST_CONTENT_SPAM_DETECTED);
    }
    post.updateSentitment(sentiment);
    postRepository.save(post);
  }

  @Async
  public int predictSentiment(HttpEntity<Map<String, Object>> request, Post post) {
    try {
      var response = restTemplate.postForEntity(SENTIMENT_PREDICT_ENDPOINT, request,
          SentimentPredictionResponse.class);
      return response.getBody().predictions().getFirst();
    } catch (ResourceAccessException e) {
      log.warn("Sentiment prediction service is not available");
    }
    return -1;
  }

  @Async
  @EventListener
  public void handleUpdateCommentCount(CommentCreatedEvent event) {
    postRepository.incrementCommentCount(event.postId());
  }

  @EventListener
  @Async
  public void handleLikePost(LikePostEvent event) {
    postRepository.incrementLikeCount(event.id());
  }

  @EventListener
  @Async
  public void handleUnlikePost(UnlikePostEvent event) {
    postRepository.decrementLikeCount(event.id());
  }

  @Async
  @EventListener
  public void handleViewPost(ViewPostEvent event) {
    postRepository.incrementViewCount(event.id());
  }
}
