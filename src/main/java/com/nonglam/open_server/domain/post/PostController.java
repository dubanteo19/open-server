package com.nonglam.open_server.domain.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.post.dto.request.PostCreateRequest;
import com.nonglam.open_server.domain.post.dto.request.PostUpdateRequest;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.exception.ApiException;
import com.nonglam.open_server.security.CustomUserDetail;
import com.nonglam.open_server.shared.ErrorCode;
import com.nonglam.open_server.shared.PagedResponse;
import com.nonglam.open_server.shared.ratelimiter.PostRateLimiterService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("api/v1/posts")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostController {
  PostService postService;
  OpenerService openerService;
  PostRateLimiterService postRateLimiterService;

  @GetMapping("/{postId}")
  public ResponseEntity<APIResponse<PostResponse>> getPostById(@PathVariable Long postId) {
    var res = postService.getPostById(postId);
    return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("fetched post", res));
  }

  @PostMapping("/{postId}/like")
  public ResponseEntity<APIResponse<Long>> likePost(@PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    var userId = userDetail.getUser().getId();
    Long likedPostId = postService.likePost(postId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("post liked", likedPostId));
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<APIResponse<Void>> deletePost(@PathVariable Long postId) {
    postService.deletePost(postId);
    return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("deleted post"));
  }

  @PutMapping
  public ResponseEntity<APIResponse<PostResponse>> updatePost(@RequestBody PostUpdateRequest request) {
    PostResponse response = postService.updatePost(request);
    return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("updated post", response));
  }

  @PostMapping
  public ResponseEntity<APIResponse<PostResponse>> createPost(@RequestBody PostCreateRequest request,
      @AuthenticationPrincipal CustomUserDetail user) {
    var openerId = user.getUser().getId();
    if (!postRateLimiterService.isAllow(openerId)) {
      openerService.flagAsSpammer(openerId);
      throw new ApiException("Request rate limited", ErrorCode.REQUEST_SPAM_DETECTED);
    }
    var response = postService.createPost(request, openerId);
    return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("created post", response));
  }

  @GetMapping
  public ResponseEntity<APIResponse<PagedResponse<PostResponse>>> getPosts(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PagedResponse<PostResponse> response = postService.getPosts(page, size);
    return ResponseEntity.ok(APIResponse.success("Fetched posts", response));
  }
}
