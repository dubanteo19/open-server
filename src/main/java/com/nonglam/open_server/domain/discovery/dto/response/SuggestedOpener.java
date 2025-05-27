package com.nonglam.open_server.domain.discovery.dto.response;

public record SuggestedOpener(
    Long id,
    String username,
    String displayName,
    String avatarUrl,
    String bio,
    boolean verified,
    boolean followed
) {
}
