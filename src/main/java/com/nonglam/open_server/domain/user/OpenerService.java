package com.nonglam.open_server.domain.user;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.post.Post;
import com.nonglam.open_server.domain.post.PostMapper;
import com.nonglam.open_server.domain.post.PostRepository;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.postlike.PostLike;
import com.nonglam.open_server.domain.user.dto.request.OpenerUpdateRequest;
import com.nonglam.open_server.domain.user.dto.response.OpenerDetail;
import com.nonglam.open_server.exception.ApiException;
import com.nonglam.open_server.exception.ResourceNotFoundException;
import com.nonglam.open_server.shared.ErrorCode;
import com.nonglam.open_server.shared.PageMapper;
import com.nonglam.open_server.shared.PagedResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OpenerService {
  OpenerRepository openerRepository;
  PostRepository postRepository;
  OpenerMapper openerMapper;
  PageMapper pageMapper;
  PostMapper postMapper;

  public Opener findById(Long id) {
    return openerRepository
        .findById(id)
        .orElseThrow(ResourceNotFoundException::openerNotFound);
  }

  public Opener findByUsername(String username) {
    return openerRepository
        .findByUsername(username)
        .orElseThrow(ResourceNotFoundException::openerNotFound);
  }

  public OpenerDetail getOpenerDetail(String username) {
    var opener = findByUsername(username);
    return openerMapper.toOpenerDetail(opener);
  }

  public PagedResponse<PostResponse> getOpenerPosts(String username, int page, int size) {
    var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    var postPage = postRepository.findByAuthor_UsernameAndDeleted(username, false, pageable);
    List<PostResponse> postResponses = postPage
        .getContent()
        .stream()
        .map(postMapper::toPostResponse)
        .toList();
    return pageMapper.toPagedResponse(postPage, postResponses);
  }

  public OpenerDetail updateOpener(Long openerId, OpenerUpdateRequest request) {
    var currentOpener = findById(openerId);
    openerMapper.applyUpdateToEntity(currentOpener, request);
    var savedOpener = openerRepository.save(currentOpener);
    return openerMapper.toOpenerDetail(savedOpener);
  }

  public OpenerDetail updateAvatar(Long openerId, String avatarUrl) {
    var currentOpener = findById(openerId);
    currentOpener.setAvatarUrl(avatarUrl);
    var savedOpener = openerRepository.save(currentOpener);
    return openerMapper.toOpenerDetail(savedOpener);
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

  public List<Long> getLikedPostIds(Long openerId) {
    return findById(openerId).getPostLikes()
        .stream()
        .map(PostLike::getPost)
        .map(Post::getId)
        .toList();
  }

}
