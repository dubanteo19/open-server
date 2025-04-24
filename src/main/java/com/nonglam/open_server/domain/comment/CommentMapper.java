package com.nonglam.open_server.domain.comment;

import com.nonglam.open_server.domain.comment.dto.requset.CommentCreateRequest;
import com.nonglam.open_server.domain.comment.dto.response.CommentResponse;
import com.nonglam.open_server.domain.post.Post;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.domain.user.OpenerMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentMapper {
    OpenerMapper openerMapper;

    public Comment toComment(CommentCreateRequest request, Opener opener, Post post) {
        var comment = new Comment();
        comment.setContent(request.payload().content());
        comment.setPost(post);
        comment.setAuthor(opener);
        return comment;
    }

    public CommentResponse toCommentResponse(Comment comment) {
        var author = openerMapper.toOpenerResponse(comment.getAuthor());
        return new CommentResponse(comment.getId(), comment.getContent(), author, comment.getUpdatedAt());
    }
}
