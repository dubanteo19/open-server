package com.nonglam.open_server.domain.comment;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.comment.dto.requset.CommentCreateRequest;
import com.nonglam.open_server.domain.comment.dto.response.CommentResponse;
import com.nonglam.open_server.domain.comment.event.CommentCreatedEvent;
import com.nonglam.open_server.domain.notification.NotificationService;
import com.nonglam.open_server.domain.post.PostRepository;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.shared.PageMapper;
import com.nonglam.open_server.shared.PagedResponse;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentService {
  CommentRepository commentRepository;
  PageMapper pageMapper;
  CommentMapper commentMapper;
  PostRepository postRepository;
  OpenerService openerService;
  NotificationService notificationService;
  ApplicationEventPublisher eventPublisher;

  @Transactional
  public CommentResponse createComment(CommentCreateRequest request, Long currentOpenerId) {
    var post = postRepository.getReferenceById(request.postId());
    var opener = openerService.findById(request.authorId());
    var comment = commentMapper.toComment(request, opener, post);
    var savedComment = commentRepository.save(comment);
    var commentResponse = commentMapper.toCommentResponse(savedComment);
    eventPublisher.publishEvent(new CommentCreatedEvent(post.getId(),
        commentResponse, opener.getUsername()));
    if (currentOpenerId != post.getAuthor().getId()) {
      String message = opener.getUsername() + " has just commented on your post " + post.getId();
      notificationService.saveNotification(message, post.getAuthor());
    }
    return commentResponse;
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
