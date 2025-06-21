package com.nonglam.open_server.domain.postlike;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.notification.NotificationService;
import com.nonglam.open_server.domain.notification.eventlistener.event.NotificationEvent;
import com.nonglam.open_server.domain.post.PostService;
import com.nonglam.open_server.domain.post.event.LikePostEvent;
import com.nonglam.open_server.domain.post.event.UnlikePostEvent;
import com.nonglam.open_server.domain.postlike.dto.response.PostLikeResponse;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.exception.ResourceNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class PostLikeService {
  PostLikeRepository postLikeRepository;
  PostService postService;
  OpenerService openerService;
  PostLikeMapper postLikeMapper;
  ApplicationEventPublisher eventPublisher;
  NotificationService notificationService;

  public Long unlikePost(Long postId, Long userId) {
    var postLike = postLikeRepository
        .findByPostIdAndOpenerId(postId, userId)
        .orElseThrow(() -> new ResourceNotFoundException("postlike not found"));
    postLikeRepository.delete(postLike);
    eventPublisher.publishEvent(new UnlikePostEvent(postId));
    return postId;
  }

  public Long likePost(Long postId, Long userId) {
    var post = postService.findById(postId);
    var opener = openerService.findById(userId);
    var postLike = PostLike.builder().post(post).opener(opener).build();
    postLikeRepository.save(postLike);
    eventPublisher.publishEvent(new LikePostEvent(postId));
    if (userId != post.getAuthor().getId()) {
      String message = opener.getUsername() + " has just liked your post " + post.getId();
      notificationService.saveNotification(message, post.getAuthor());
    }
    return postId;
  }

  public List<PostLikeResponse> getPostLikes(Long postId) {
    var sort = Sort.by("likedAt").descending();
    var postLikeList = postLikeRepository.findAllByPostId(postId, sort);
    return postLikeMapper.toPostLikeResponseList(postLikeList);

  }
}
