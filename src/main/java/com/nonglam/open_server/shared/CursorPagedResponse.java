package com.nonglam.open_server.shared;

import java.util.List;

public record CursorPagedResponse<T>(
    List<T> items,
    boolean hasMore,
    Long nextCursor) {
}
