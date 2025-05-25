package com.nonglam.open_server.domain.post;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.postbookmark.PostBookmarkRepository;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.shared.CursorPagedResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostQueryService {
  private final PostRepository postRepository;
  private final PostBookmarkRepository postBookmarkRepository;
  private final OpenerService openerService;
  private final PostMetaDataEnricher postMetaDataEnricher;
  private static final int FIXED_SIZE = 5;
  private static final Sort sort = Sort.by("id").descending();

  private PageRequest getPageRequest() {
    return PageRequest.of(0, FIXED_SIZE + 1, sort);
  }

  public CursorPagedResponse<PostResponse> getPostByAuthor(String username, Long after, Long openerId) {
    List<Post> fetchedPosts = postRepository
        .findByAuthorAndIdLessThan(username, after, getPageRequest());
    var posts = paginate(fetchedPosts);
    Long nextCursor = getNextCursor(fetchedPosts);
    var opener = openerService.findById(openerId);
    var enrichedPosts = postMetaDataEnricher.enrich(posts, opener);
    return new CursorPagedResponse<>(enrichedPosts, nextCursor != null, nextCursor);
  }

  public CursorPagedResponse<PostResponse> getFeed(Long after, Long openerId) {
    List<Post> fetchedPosts = postRepository.findTopNByIdLessThan(after, getPageRequest());
    var posts = paginate(fetchedPosts);
    Long nextCursor = getNextCursor(fetchedPosts);
    var opener = openerService.findById(openerId);
    var enrichedPosts = postMetaDataEnricher.enrich(posts, opener);
    return new CursorPagedResponse<>(enrichedPosts, nextCursor != null, nextCursor);
  }

  private Long getNextCursor(List<Post> fetchedPosts) {
    return fetchedPosts.size() > FIXED_SIZE ? fetchedPosts.get(FIXED_SIZE - 1).getId() : null;
  }

  private List<Post> paginate(List<Post> fetchedPosts) {
    return fetchedPosts.size() > FIXED_SIZE ? fetchedPosts.subList(0, FIXED_SIZE) : fetchedPosts;
  }

  public CursorPagedResponse<PostResponse> getBookmarkedPosts(Long openerId, Long after) {
    List<Post> fetchedPosts = postBookmarkRepository
        .findBookmarkedPostsByOpenerId(openerId, after, getPageRequest());
    var posts = paginate(fetchedPosts);
    Long nextCursor = getNextCursor(fetchedPosts);
    var opener = openerService.findById(openerId);
    var enrichedPosts = postMetaDataEnricher.enrich(posts, opener);
    return new CursorPagedResponse<>(enrichedPosts, nextCursor != null, nextCursor);
  }

}
