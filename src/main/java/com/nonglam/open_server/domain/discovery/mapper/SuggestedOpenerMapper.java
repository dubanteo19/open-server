package com.nonglam.open_server.domain.discovery.mapper;

import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.discovery.dto.response.SuggestedOpener;
import com.nonglam.open_server.domain.user.Opener;

@Component
public class SuggestedOpenerMapper {

  public SuggestedOpener toSuggestedOpener(Opener opener, boolean followed) {
    return new SuggestedOpener(
        opener.getId(),
        opener.getUsername(),
        opener.getDisplayName(),
        opener.getAvatarUrl(),
        opener.getBio(),
        opener.isVerified(),
        followed);
  }

}
