package com.nonglam.open_server.domain.discovery;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.discovery.dto.response.SuggestedOpener;
import com.nonglam.open_server.security.CustomUserDetail;
import com.nonglam.open_server.shared.PagedResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/discovery")
@RequiredArgsConstructor
public class DiscoveryController {
  private final DiscoveryService discoveryService;

  @GetMapping
  public ResponseEntity<APIResponse<PagedResponse<SuggestedOpener>>> findOpeners(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long openerId = userDetail.getUser().getId();
    var response = discoveryService.findOpenersExcept(page, size, openerId);
    return ResponseEntity.ok(APIResponse.success("discover openers", response));
  }
}
