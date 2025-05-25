package com.nonglam.open_server.domain.post;

import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.post.dto.request.PostCreateRequest;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.domain.user.OpenerMapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostMapper {
  OpenerMapper openerMapper;

  public Post toPost(PostCreateRequest request, Opener author, long simHash) {
    Post post = new Post(request.payload().content(), author, simHash);
    return post;
  }

  public PostResponse toPostResponse(Post post, boolean bookmarked, boolean liked, boolean mine) {
    var opener = post.getAuthor();
    var author = openerMapper.toOpenerResponse(opener);
    return new PostResponse(
        post.getId(),
        author,
        post.getContent(),
        post.getViewCount(),
        post.getLikeCount(),
        post.getCommentCount(),
        post.getSentiment(),
        liked,
        bookmarked,
        mine,
        post.getCreatedAt());
  }

  public PostResponse toPostResponse(Post post) {
    return toPostResponse(post, false, false, false);
  }
}
