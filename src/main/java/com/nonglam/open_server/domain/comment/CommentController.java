package com.nonglam.open_server.domain.comment;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.comment.dto.requset.CommentCreateRequest;
import com.nonglam.open_server.domain.comment.dto.response.CommentResponse;
import com.nonglam.open_server.shared.PagedResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("api/v1/posts/{postId}/comments")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentController {
    CommentService commentService;

    @GetMapping
    public ResponseEntity<APIResponse<PagedResponse<CommentResponse>>> getCommentByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        var response = commentService.getCommentByPostId(postId, page, size);
        return ResponseEntity.ok(APIResponse.success("Comments fetched", response));
    }

    @PostMapping
    public ResponseEntity<APIResponse<CommentResponse>> createComment(@RequestBody CommentCreateRequest request) {
        var response = commentService.createComment(request);
        return ResponseEntity.ok(APIResponse.success("Comment created", response));
    }
}
