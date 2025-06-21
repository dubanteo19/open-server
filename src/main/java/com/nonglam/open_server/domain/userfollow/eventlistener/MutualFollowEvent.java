package com.nonglam.open_server.domain.userfollow.eventlistener;

public record MutualFollowEvent(
    Long followerId,
    Long followedId) {
}
