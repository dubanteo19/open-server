package com.nonglam.open_server.domain.post;

import com.nonglam.open_server.domain.post.dto.request.PostCreateRequest;
import com.nonglam.open_server.domain.post.dto.request.PostUpdateRequest;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.user.OpenerRepository;
import com.nonglam.open_server.shared.PageMapper;
import com.nonglam.open_server.shared.PagedResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostService {
    PostRepository postRepository;
    OpenerRepository openerRepository;
    PostMapper postMapper;
    PageMapper pageMapper;

    public PostResponse toResponse(Post post) {
        return postMapper.toPostResponse(post);
    }

    public PagedResponse<PostResponse> getPosts(int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        var postPage = postRepository.findAllByDeleted(false, pageable);

        List<PostResponse> postResponses = postPage
                .getContent()
                .stream()
                .map(this::toResponse)
                .toList();
        return pageMapper.toPagedResponse(postPage, postResponses);
    }

    public void deletePost(Long postId) {
        var currentPost = postRepository
                .findById(postId)
                .orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")));
        currentPost.setDeleted(true);
        postRepository.save(currentPost);
    }

    public PostResponse createPost(PostCreateRequest request) {
        var post = postMapper.toPost(request);
        System.out.println(post);
        var opener = openerRepository
                .findById(request.openerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Opener not found"));
        post.setAuthor(opener);
        var savedPost = postRepository.save(post);
        return postMapper.toPostResponse(savedPost);
    }

    public PostResponse updatePost(PostUpdateRequest request) {
        var postId = request.postId();
        var content = request.payload().content();
        var currentPost = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        currentPost.setContent(content);
        var savedPost = postRepository.save(currentPost);
        return postMapper.toPostResponse(savedPost);
    }
}
