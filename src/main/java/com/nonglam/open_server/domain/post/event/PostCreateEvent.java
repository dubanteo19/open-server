package com.nonglam.open_server.domain.post.event;

import com.nonglam.open_server.domain.post.Post;

public record PostCreateEvent(
    Post post,
    Long openerId) {
}
