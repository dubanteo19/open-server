package com.nonglam.open_server.domain.user;

import com.nonglam.open_server.domain.post.PostMapper;
import com.nonglam.open_server.domain.post.PostRepository;
import com.nonglam.open_server.domain.post.dto.response.PostResponse;
import com.nonglam.open_server.domain.user.dto.request.OpenerUpdateRequest;
import com.nonglam.open_server.domain.user.dto.response.OpenerDetail;
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
public class OpenerService {
    OpenerRepository openerRepository;
    PostRepository postRepository;
    OpenerMapper openerMapper;
    PageMapper pageMapper;
    PostMapper postMapper;

    public OpenerDetail getOpenerDetail(String username) {
        var opener = openerRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return openerMapper.toOpenerDetail(opener);
    }

    public PagedResponse<PostResponse> getOpenerPosts(String username, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        var postPage = postRepository.findByAuthor_UsernameAndDeleted(username, false, pageable);
        List<PostResponse> postResponses = postPage
                .getContent()
                .stream()
                .map(postMapper::toPostResponse)
                .toList();
        return pageMapper.toPagedResponse(postPage, postResponses);
    }

    public OpenerDetail updateOpener(Long openerId, OpenerUpdateRequest request) {
        var currentOpener = openerRepository
                .findById(openerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        openerMapper.updateOpener(currentOpener, request);
        var savedOpener = openerRepository.save(currentOpener);
        return openerMapper.toOpenerDetail(savedOpener);
    }
}
