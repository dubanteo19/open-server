package com.nonglam.open_server.domain.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.discovery.dto.response.SuggestedOpener;
import com.nonglam.open_server.domain.discovery.enricher.SuggestedOpenerEnricher;
import com.nonglam.open_server.domain.user.dto.request.OpenerUpdateRequest;
import com.nonglam.open_server.domain.user.dto.response.OpenerDetail;
import com.nonglam.open_server.domain.user.dto.response.OpenerResponse;
import com.nonglam.open_server.domain.userfollow.OpenerFollow;
import com.nonglam.open_server.domain.userfollow.OpenerFollowRepository;
import com.nonglam.open_server.exception.ApiException;
import com.nonglam.open_server.exception.ResourceNotFoundException;
import com.nonglam.open_server.shared.ErrorCode;
import com.nonglam.open_server.shared.PageMapper;
import com.nonglam.open_server.shared.PagedResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenerService {
  private final OpenerRepository openerRepository;
  private final OpenerFollowRepository openerFollowRepository;
  private final OpenerMapper openerMapper;
  private final PageMapper pageMapper;
  private final SuggestedOpenerEnricher suggestedOpenerEnricher;

  public Opener findById(Long id) {
    return openerRepository
        .findById(id)
        .orElseThrow(ResourceNotFoundException::openerNotFound);
  }

  public Opener findByEmail(String email) {
    return openerRepository
        .findByEmail(email)
        .orElseThrow(ResourceNotFoundException::openerNotFound);
  }

  public Opener findByUsername(String username) {
    return openerRepository
        .findByUsername(username)
        .orElseThrow(ResourceNotFoundException::openerNotFound);
  }

  public OpenerDetail getOpenerDetail(String username, Long currentOpenerId) {
    var opener = findByUsername(username);
    boolean followed = openerFollowRepository.existsByFollowedIdAndFollowerId(opener.getId(), currentOpenerId);
    return openerMapper.toOpenerDetail(opener, followed);
  }

  public void updateOpener(Long openerId, OpenerUpdateRequest request) {
    var currentOpener = findById(openerId);
    openerMapper.applyUpdateToEntity(currentOpener, request);
    openerRepository.save(currentOpener);
  }

  public void updateAvatar(Long openerId, String avatarUrl) {
    var currentOpener = findById(openerId);
    currentOpener.setAvatarUrl(avatarUrl);
    openerRepository.save(currentOpener);
  }

  public void flagAsSpammer(Long openerId) {
    var opener = findById(openerId);
    opener.flagAsSpammer();
    if (opener.getSpamFlagCount() >= 3) {
      opener.setBlocked(true);
      openerRepository.save(opener);
      SecurityContextHolder.clearContext();
      throw new ApiException("Your account's blocked", ErrorCode.OPENER_BLOCKED);
    }
    openerRepository.save(opener);
  }

  public void follow(Long id, Long currentOpenerId) {
    var followed = findById(id);
    var follower = findById(currentOpenerId);
    var openerFollow = OpenerFollow
        .builder()
        .followed(followed)
        .follower(follower)
        .build();
    openerFollowRepository.save(openerFollow);
  }

  public PagedResponse<SuggestedOpener> getFollowers(String username, Long currentOpenerId,
      Pageable pageable) {
    var openerId = findByUsername(username).getId();
    var page = openerFollowRepository.findAllByFollowedId(openerId, pageable);
    var currentOpener = findById(currentOpenerId);
    var suggestedOpenerList = suggestedOpenerEnricher.enrich(page.getContent(), currentOpener);
    return pageMapper.toPagedResponse(page, suggestedOpenerList);
  }

  public PagedResponse<SuggestedOpener> getFollowing(String username, Long currentOpenerId,
      Pageable pageable) {
    var openerId = findByUsername(username).getId();
    var page = openerFollowRepository.findAllByFollowerId(openerId, pageable);
    var currentOpener = findById(currentOpenerId);
    var suggestedOpenerList = suggestedOpenerEnricher.enrich(page.getContent(), currentOpener);
    return pageMapper.toPagedResponse(page, suggestedOpenerList);
  }

  public void unfollowOpener(Long id, Long currentOpenerId) {
    var openerFollow = openerFollowRepository
        .findByFollowerIdAndFollowedId(currentOpenerId, id)
        .orElseThrow(() -> new ResourceNotFoundException("Opener Follow not found"));
    openerFollowRepository.delete(openerFollow);

  }

  public PagedResponse<OpenerResponse> getFriends(Long currentOpenerId, Pageable pageable) {
    var page = openerRepository.findMutualFriends(currentOpenerId, pageable);
    var openerResponseList = page.map(openerMapper::toOpenerResponse).getContent();
    return pageMapper.toPagedResponse(page, openerResponseList);

  }

}
