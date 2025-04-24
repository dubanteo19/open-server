package com.nonglam.open_server.domain.post;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.post.dto.request.PostCreateRequest;
import com.nonglam.open_server.domain.post.dto.request.PostUpdateRequest;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.user.OpenerRepository;
import com.nonglam.open_server.exception.ResourceNotFoundException;
import com.nonglam.open_server.shared.PageMapper;
import com.nonglam.open_server.shared.PagedResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostService {
  PostRepository postRepository;
  OpenerRepository openerRepository;
  PostMapper postMapper;
  PageMapper pageMapper;

  public PostResponse getPostById(Long postId) {
    var post = findById(postId);
    return postMapper.toPostResponse(post);
  }

  public PagedResponse<PostResponse> getPosts(int page, int size) {
    var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    var postPage = postRepository.findAllByDeleted(false, pageable);
    List<PostResponse> postResponses = postPage
        .getContent()
        .stream()
        .map(postMapper::toPostResponse)
        .toList();
    return pageMapper.toPagedResponse(postPage, postResponses);
  }

  public Post findById(Long postId) {
    return postRepository
        .findById(postId)
        .orElseThrow((() -> new ResourceNotFoundException("Post not found")));
  }

  public void deletePost(Long postId) {
    var currentPost = findById(postId);
    currentPost.setDeleted(true);
    postRepository.save(currentPost);
  }

  public PostResponse createPost(PostCreateRequest request) {
    var opener = openerRepository
        .findById(request.openerId())
        .orElseThrow(ResourceNotFoundException::openerNotFound);
    var post = postMapper.toPost(request, opener);
    var savedPost = postRepository.save(post);
    return postMapper.toPostResponse(savedPost);
  }

  public PostResponse updatePost(PostUpdateRequest request) {
    var postId = request.postId();
    var content = request.payload().content();
    var currentPost = findById(postId);
    currentPost.setContent(content);
    var savedPost = postRepository.save(currentPost);
    return postMapper.toPostResponse(savedPost);
  }
}
