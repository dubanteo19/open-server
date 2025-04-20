package com.nonglam.open_server.domain.post;

import com.nonglam.open_server.domain.post.dto.request.PostCreateRequest;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.user.dto.response.OpenerResponse;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public Post toPost(PostCreateRequest request) {
        Post post = new Post();
        post.setContent(request.payload().content());
        post.setDeleted(false);
        return post;
    }

    public PostResponse toPostResponse(Post post) {
        var opener = post.getAuthor();
        var author = new OpenerResponse(
                opener.getId(),
                opener.getUsername(),
                opener.getDisplayName(),
                opener.isVerified(),
                opener.getAvatarUrl()
        );
        return new PostResponse(
                post.getId(),
                author,
                post.getContent(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getCreatedAt()
        );
    }
}
