package com.nonglam.open_server.domain.postbookmark;

import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.post.PostService;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostBookmarkService {
  private final PostBookmarkRepository postBookmarkRepository;
  private final PostService postService;
  private final OpenerService openerService;

  public void bookmarkPost(Long postId, Long openerId) {
    var post = postService.findById(postId);
    var opener = openerService.findById(openerId);
    var postBookmark = PostBookmark
        .builder()
        .post(post)
        .opener(opener).build();
    postBookmarkRepository.save(postBookmark);
  }

  public void unBookmarkPost(Long postId, Long openerId) {
    var postBookmark = postBookmarkRepository
        .findByPostIdAndOpenerId(postId, openerId)
        .orElseThrow(() -> new ResourceNotFoundException("post boorkmark not found"));
    postBookmarkRepository.delete(postBookmark);
  }
}
