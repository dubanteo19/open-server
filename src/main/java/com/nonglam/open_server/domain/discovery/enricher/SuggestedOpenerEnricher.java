package com.nonglam.open_server.domain.discovery.enricher;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.j2objc.annotations.UsedByNative;
import com.nonglam.open_server.domain.discovery.dto.response.SuggestedOpener;
import com.nonglam.open_server.domain.discovery.mapper.SuggestedOpenerMapper;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.domain.user.OpenerMapper;
import com.nonglam.open_server.domain.user.dto.response.OpenerDetail;
import com.nonglam.open_server.domain.userfollow.OpenerFollowRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuggestedOpenerEnricher {
  private final SuggestedOpenerMapper suggestedOpenerMapper;
  private final OpenerFollowRepository openerFollowRepository;
  private OpenerMapper openerMapper;

  public List<SuggestedOpener> enrich(List<Opener> openers, Opener currentOpener) {
    List<Long> openerIds = openers.stream().map(Opener::getId).toList();
    Set<Long> followedIds = openerFollowRepository.findFollowedIds(currentOpener.getId(), openerIds);
    return openers.stream().map(opener -> toResponse(opener, followedIds)).toList();
  }

  public SuggestedOpener toResponse(Opener opener, Set<Long> followedIds) {
    boolean followed = followedIds.contains(opener.getId());
    return suggestedOpenerMapper.toSuggestedOpener(opener, followed);
  }

  public OpenerDetail enrich(Opener opener, Opener currentOpener) {
    boolean followed = openerFollowRepository
        .existsByFollowedIdAndFollowerId(opener.getId(), currentOpener.getId());
    return openerMapper.toOpenerDetail(opener, followed);
  }
}
