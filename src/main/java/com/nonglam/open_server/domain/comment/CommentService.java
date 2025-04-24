package com.nonglam.open_server.domain.comment;

import com.nonglam.open_server.domain.comment.dto.requset.CommentCreateRequest;
import com.nonglam.open_server.domain.comment.dto.response.CommentResponse;
import com.nonglam.open_server.domain.comment.event.CommentCreatedEvent;
import com.nonglam.open_server.domain.post.PostService;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.shared.PageMapper;
import com.nonglam.open_server.shared.PagedResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentService {
  CommentRepository commentRepository;
  PageMapper pageMapper;
  CommentMapper commentMapper;
  PostService postService;
  OpenerService openerService;
  ApplicationEventPublisher eventPublisher;

  public CommentResponse createComment(CommentCreateRequest request) {
    var post = postService.findById(request.postId());
    var opener = openerService.findById(request.authorId());
    var comment = commentMapper.toComment(request, opener, post);
    var savedComment = commentRepository.save(comment);
    eventPublisher.publishEvent(new CommentCreatedEvent(post.getId(),
        savedComment.getId(), opener.getUsername()));
    return commentMapper.toCommentResponse(savedComment);
  }

  public PagedResponse<CommentResponse> getCommentByPostId(Long postId, int page, int size) {
    var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    var commentPage = commentRepository.findAllByPostId(postId, pageable);
    var commentList = commentPage
        .stream()
        .map(commentMapper::toCommentResponse)
        .toList();
    return pageMapper.toPagedResponse(commentPage, commentList);
  }
}
