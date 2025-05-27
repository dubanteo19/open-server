package com.nonglam.open_server.domain.user.dto.response;

public record OpenerDetail(
    OpenerResponse summary,
    String bio,
    String location,
    String joinDate,
    int following,
    int followers,
    boolean followed) {
}
