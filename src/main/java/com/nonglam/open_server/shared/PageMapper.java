package com.nonglam.open_server.shared;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageMapper {
  public <T> PagedResponse<T> toPagedResponse(Page<?> page, List<T> content) {
    return new PagedResponse<T>(
        content,
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(), page.isLast());
  }
}
