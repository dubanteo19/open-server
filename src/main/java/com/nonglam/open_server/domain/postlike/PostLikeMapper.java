package com.nonglam.open_server.domain.postlike;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.postlike.dto.response.PostLikeResponse;
import com.nonglam.open_server.domain.user.OpenerMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostLikeMapper {
  private final OpenerMapper openerMapper;

  public List<PostLikeResponse> toPostLikeResponseList(List<PostLike> postLikeList) {
    return postLikeList.stream().map(this::toPostLikeResponse).toList();
  }

  public PostLikeResponse toPostLikeResponse(PostLike postLike) {
    var openerResponse = openerMapper.toOpenerResponse(postLike.getOpener());
    return new PostLikeResponse(openerResponse, postLike.getLikedAt());
  }

}
