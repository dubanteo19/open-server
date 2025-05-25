package com.nonglam.open_server.domain.post;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.post.dto.request.PostCreateRequest;
import com.nonglam.open_server.domain.post.dto.request.PostUpdateRequest;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.post.event.PostCreateEvent;
import com.nonglam.open_server.domain.post.event.ViewPostEvent;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.exception.ApiException;
import com.nonglam.open_server.exception.ResourceNotFoundException;
import com.nonglam.open_server.shared.ErrorCode;
import com.nonglam.open_server.shared.SimHashUtil;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostService {
  PostRepository postRepository;
  OpenerService openerService;
  PostMapper postMapper;
  PostMetaDataEnricher postMetaDataEnricher;
  ApplicationEventPublisher eventPublisher;

  public PostResponse getPostById(Long postId, Long openerId) {
    var post = findById(postId);
    var opener = openerService.findById(openerId);
    return postMetaDataEnricher.enrich(post, opener);
  }

  public Post findById(Long postId) {
    var post = postRepository
        .findById(postId)
        .orElseThrow((() -> new ResourceNotFoundException("Post not found")));
    if (post.isDeleted()) {
      throw new IllegalStateException("Post delted");
    }
    return post;
  }

  public void deletePost(Long postId) {
    var currentPostReference = postRepository.getReferenceById(postId);
    currentPostReference.markAsDeleted();
    postRepository.save(currentPostReference);
  }

  public PostResponse createPost(PostCreateRequest request, Long openerId) {
    var opener = openerService.findById(openerId);
    var simHash = SimHashUtil.simHash(request.payload().content());
    if (isDuplicateSpam(simHash, openerId)) {
      throw new ApiException("Post spam detected", ErrorCode.POST_SPAM_DETECTED);
    }
    var post = postMapper.toPost(request, opener, simHash);
    var savedPost = postRepository.save(post);
    eventPublisher.publishEvent(new PostCreateEvent(savedPost, savedPost.getAuthor().getId()));
    return postMapper.toPostResponse(savedPost);
  }

  public boolean isDuplicateSpam(long simHash, Long openerId) {
    final int THRESHOLD = 10;
    var recentPosts = postRepository.findByAuthorIdOrderByCreatedAtDesc(openerId, PageRequest.of(0, 10));
    int similarCount = 0;
    for (Post post : recentPosts) {
      int distance = SimHashUtil.hammingDistance(post.getSimHash(), simHash);
      if (distance < THRESHOLD) {
        similarCount++;
      }
    }
    return similarCount > 2;
  }

  public PostResponse updatePost(PostUpdateRequest request) {
    Long postId = request.postId();
    String content = request.payload().content();
    var currentPost = findById(postId);
    currentPost.updateContent(content);
    var savedPost = postRepository.save(currentPost);
    return postMapper.toPostResponse(savedPost);
  }

  public void viewPost(Long postId) {
    eventPublisher.publishEvent(new ViewPostEvent(postId));
  }

}
