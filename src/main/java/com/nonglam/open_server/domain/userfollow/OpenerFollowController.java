package com.nonglam.open_server.domain.userfollow;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.discovery.dto.response.SuggestedOpener;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.security.CustomUserDetail;
import com.nonglam.open_server.shared.PagedResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/openers")
@RequiredArgsConstructor
public class OpenerFollowController {
  private final OpenerService openerService;

  @GetMapping("/{username}/following")
  public ResponseEntity<APIResponse<PagedResponse<SuggestedOpener>>> getFollowing(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size,
      @PathVariable String username,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var pageable = PageRequest.of(page, size, Sort.by("createdAt"));
    var response = openerService.getFollowing(username, currentOpenerId, pageable);
    return ResponseEntity.ok(APIResponse.success("opener's following fetched", response));
  }

  @GetMapping("/{username}/followers")
  public ResponseEntity<APIResponse<PagedResponse<SuggestedOpener>>> getFollowers(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size,
      @PathVariable String username,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var pageable = PageRequest.of(page, size, Sort.by("createdAt"));
    var response = openerService.getFollowers(username, currentOpenerId, pageable);
    return ResponseEntity.ok(APIResponse.success("user's followers fetched", response));
  }

  @DeleteMapping("/{id}/unfollow")
  public ResponseEntity<APIResponse<Void>> unfollowOpener(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    openerService.unfollowOpener(id, currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("follow openers"));
  }

  @PostMapping("/{id}/follow")
  public ResponseEntity<APIResponse<Void>> followOpener(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    openerService.follow(id, currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("follow openers"));
  }
}
