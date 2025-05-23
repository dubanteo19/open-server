package com.nonglam.open_server.domain.postlike;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.post.PostService;
import com.nonglam.open_server.domain.post.event.LikePostEvent;
import com.nonglam.open_server.domain.post.event.UnlikePostEvent;
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
  ApplicationEventPublisher eventPublisher;

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
    return postId;
  }
}
