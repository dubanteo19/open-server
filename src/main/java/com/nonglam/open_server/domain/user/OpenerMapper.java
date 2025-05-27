package com.nonglam.open_server.domain.user;

import com.nonglam.open_server.domain.user.dto.request.OpenerUpdateRequest;
import com.nonglam.open_server.domain.user.dto.response.OpenerDetail;
import com.nonglam.open_server.domain.user.dto.response.OpenerResponse;
import org.springframework.stereotype.Component;

@Component
public class OpenerMapper {
  public void applyUpdateToEntity(Opener currentOpener, OpenerUpdateRequest request) {
    currentOpener.updateInfo(request.bio(), request.displayName(), request.location());
  }

  public OpenerDetail toOpenerDetail(Opener opener, boolean followed) {
    var openerSummary = toOpenerResponse(opener);
    return new OpenerDetail(openerSummary,
        opener.getBio(),
        opener.getLocation(),
        opener.getJoinDate().toString(),
        opener.getFollowing().size(),
        opener.getFollowers().size(),
        followed);
  }

  public OpenerResponse toOpenerResponse(Opener opener) {
    return new OpenerResponse(
        opener.getId(),
        opener.getUsername(),
        opener.getDisplayName(),
        opener.isVerified(),
        opener.getAvatarUrl());
  }
}
