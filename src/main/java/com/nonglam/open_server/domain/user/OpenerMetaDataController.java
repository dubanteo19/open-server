package com.nonglam.open_server.domain.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("api/v1/openers/metadata")
public class OpenerMetaDataController {
  OpenerService openerService;


  @GetMapping("/{openerId}/liked-post-ids")
  public ResponseEntity<APIResponse<List<Long>>> getLikedPostIds(@PathVariable Long openerId) {
    var response = openerService.getLikedPostIds(openerId);
    return ResponseEntity.ok(APIResponse.success("liked post ids fetched", response));

  }
}
