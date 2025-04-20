package com.nonglam.open_server.domain.post;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.auth.dto.request.LoginRequest;
import com.nonglam.open_server.domain.post.dto.request.PostCreateRequest;
import com.nonglam.open_server.domain.post.dto.request.PostUpdateRequest;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.shared.PagedResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/posts")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostController {
    PostService postService;

    @DeleteMapping("/{postId}")
    public ResponseEntity<APIResponse<Void>> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("deleted post"));
    }

    @PutMapping
    public ResponseEntity<APIResponse<PostResponse>> updatePost(@RequestBody PostUpdateRequest request) {
        PostResponse response = postService.updatePost(request);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("updated post", response));
    }

    @PostMapping
    public ResponseEntity<APIResponse<PostResponse>> createPost(@RequestBody PostCreateRequest request) {
        PostResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("created post", response));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PagedResponse<PostResponse>>>
    getPosts(@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<PostResponse> response = postService.getPosts(page, size);
        return ResponseEntity.ok(APIResponse.success("Fetched posts", response));
    }
}
