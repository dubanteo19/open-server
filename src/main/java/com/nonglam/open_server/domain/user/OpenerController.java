package com.nonglam.open_server.domain.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.post.PostQueryService;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.user.dto.request.OpenerUpdateRequest;
import com.nonglam.open_server.domain.user.dto.response.OpenerDetail;
import com.nonglam.open_server.security.CustomUserDetail;
import com.nonglam.open_server.shared.CursorPagedResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/openers")
@RequiredArgsConstructor
public class OpenerController {
  private final OpenerService openerService;
  private final PostQueryService postQueryService;

  @GetMapping("/{username}/posts")
  public ResponseEntity<APIResponse<CursorPagedResponse<PostResponse>>> getOpenerPosts(
      @PathVariable String username,
      @RequestParam(required = false) Long after,
      @AuthenticationPrincipal CustomUserDetail user) {
    var posts = postQueryService.getPostByAuthor(username, after, user.getUser().getId());
    return ResponseEntity.ok(APIResponse.success("fetched opener's posts", posts));
  }

  @GetMapping("/bookmarked-posts")
  public ResponseEntity<APIResponse<CursorPagedResponse<PostResponse>>> getBookmarkedPosts(
      @AuthenticationPrincipal CustomUserDetail user,
      @RequestParam(required = false) Long after) {
    var bookmarkedPosts = postQueryService.getBookmarkedPosts(user.getUser().getId(), after);
    return ResponseEntity.ok(APIResponse.success("fetched opener's likedPostIds", bookmarkedPosts));
  }

  @PutMapping
  public ResponseEntity<APIResponse<Void>> updateOpener(
      @AuthenticationPrincipal CustomUserDetail userDetail,
      @RequestBody OpenerUpdateRequest request) {
    openerService.updateOpener(userDetail.getUser().getId(), request);
    return ResponseEntity.ok(APIResponse.success("update openerDetail"));
  }

  @PutMapping("/{openerId}/avatar")
  public ResponseEntity<APIResponse<Void>> updateAvatar(
      @PathVariable Long openerId,
      @RequestBody String avatarUrl) {
    openerService.updateAvatar(openerId, avatarUrl);
    return ResponseEntity.ok(APIResponse.success("update avatar openerDetail"));
  }

  @GetMapping("/{username}")
  public ResponseEntity<APIResponse<OpenerDetail>> getOpenerDetail(
      @PathVariable String username,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var openerDetail = openerService.getOpenerDetail(username, currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("fetched openerDetail", openerDetail));
  }
}
