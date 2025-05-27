package com.nonglam.open_server.domain.discovery;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.discovery.dto.response.SuggestedOpener;
import com.nonglam.open_server.domain.discovery.enricher.SuggestedOpenerEnricher;
import com.nonglam.open_server.domain.user.OpenerRepository;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.shared.PageMapper;
import com.nonglam.open_server.shared.PagedResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscoveryService {
  private final OpenerRepository openerRepository;
  private final OpenerService openerService;
  private final PageMapper pageMapper;
  private final SuggestedOpenerEnricher suggestedOpenerEnricher;

  public PagedResponse<SuggestedOpener> findOpenersExcept(int pageNumber, int size, Long openerId) {
    var sort = Sort.by("joinDate").descending();
    var pageRequest = PageRequest.of(pageNumber, size, sort);
    var page = openerRepository.findAll(pageRequest);
    var content = page.getContent().stream().filter(o -> o.getId() != openerId).toList();
    var currentOpener = openerService.findById(openerId);
    var openerResponseList = suggestedOpenerEnricher.enrich(content, currentOpener);
    return pageMapper.toPagedResponse(page, openerResponseList);
  }
}
