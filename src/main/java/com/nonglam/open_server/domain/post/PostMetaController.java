package com.nonglam.open_server.domain.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.postbookmark.PostBookmarkService;
import com.nonglam.open_server.domain.postlike.PostLikeService;
import com.nonglam.open_server.security.CustomUserDetail;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/posts/{postId}/meta")
@RequiredArgsConstructor
public class PostMetaController {
  private final PostLikeService postLikeService;
  private final PostBookmarkService postBookmarkService;

  @PostMapping("/bookmark")
  public ResponseEntity<Void> bookmarkPost(@PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    var userId = userDetail.getUser().getId();
    postBookmarkService.bookmarkPost(postId, userId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/unbookmark")
  public ResponseEntity<Void> unBookmarkPost(@PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    var userId = userDetail.getUser().getId();
    postBookmarkService.unBookmarkPost(postId, userId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/like")
  public ResponseEntity<APIResponse<Long>> likePost(@PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    var userId = userDetail.getUser().getId();
    Long likedPostId = postLikeService.likePost(postId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("post liked", likedPostId));
  }

  @DeleteMapping("/unlike")
  public ResponseEntity<APIResponse<Long>> unlikePost(@PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    var userId = userDetail.getUser().getId();
    Long likedPostId = postLikeService.unlikePost(postId, userId);
    return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("post liked", likedPostId));
  }
}
