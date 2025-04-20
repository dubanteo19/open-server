package com.nonglam.open_server.domain.user;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.user.dto.request.OpenerUpdateRequest;
import com.nonglam.open_server.domain.user.dto.response.OpenerDetail;
import com.nonglam.open_server.shared.PagedResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/openers")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class OpenerController {
    OpenerService openerService;

    @GetMapping("/{username}/posts")
    public ResponseEntity<APIResponse<PagedResponse<PostResponse>>>
    getOpenerPosts(@PathVariable String username,
                   @RequestParam(defaultValue = "0") int page,
                   @RequestParam(defaultValue = "10") int size) {
        var posts = openerService.getOpenerPosts(username, page, size);
        return ResponseEntity.ok(APIResponse.success("fetched opener's posts", posts));
    }

    @PutMapping("/{openerId}")
    public ResponseEntity<APIResponse<OpenerDetail>> updateOpener
            (@PathVariable Long openerId, @RequestBody OpenerUpdateRequest request) {
        var openerDetail = openerService.updateOpener(openerId, request);
        return ResponseEntity.ok(APIResponse.success("fetched openerDetail", openerDetail));
    }

    @GetMapping("/{username}")
    public ResponseEntity<APIResponse<OpenerDetail>> getOpenerDetail(@PathVariable String username) {
        var openerDetail = openerService.getOpenerDetail(username);
        return ResponseEntity.ok(APIResponse.success("fetched openerDetail", openerDetail));
    }
}
