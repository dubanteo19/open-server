package com.nonglam.open_server.domain.post;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.postbookmark.PostBookmarkRepository;
import com.nonglam.open_server.domain.postlike.PostLikeRepository;
import com.nonglam.open_server.domain.user.Opener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostMetaDataEnricher {
  private final PostLikeRepository postLikeRepository;
  private final PostBookmarkRepository postBookmarkRepository;
  private final PostMapper postMapper;

  public PostResponse enrich(Post post, Opener opener) {
    Set<Long> likedPostIds = postLikeRepository.findLikedPostIds(opener.getId(), List.of(post.getId()));
    Set<Long> bookMarkedPostIds = postBookmarkRepository
        .findBookmarkedPostIds(opener.getId(), List.of(post.getId()));
    return toResponse(post, opener, likedPostIds, bookMarkedPostIds);
  }

  public List<PostResponse> enrich(List<Post> posts, Opener opener) {
    List<Long> postIds = posts.stream().map(Post::getId).toList();
    Set<Long> likedPostIds = postLikeRepository.findLikedPostIds(opener.getId(), postIds);
    Set<Long> bookMarkedPostIds = postBookmarkRepository.findBookmarkedPostIds(opener.getId(), postIds);
    return posts.stream().map(p -> toResponse(p, opener, likedPostIds, bookMarkedPostIds)).toList();
  }

  private PostResponse toResponse(Post p, Opener opener, Set<Long> likedPostIds, Set<Long> bookMarkedPostIds) {
    boolean mine = p.isAuthor(opener);
    boolean liked = likedPostIds.contains(p.getId());
    boolean bookmarked = bookMarkedPostIds.contains(p.getId());
    return postMapper.toPostResponse(p, bookmarked, liked, mine);
  }
}
