package com.nonglam.open_server.domain.post.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.post.PostRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
public class PostListener {
  PostRepository postRepository;

  @Async
  @EventListener
  public void handleViewPost(ViewPostEvent event) {
    System.out.println("I am updating post view count atm" + event.id());
    postRepository.incrementViewCount(event.id());
    System.out.println("updated ");
  }
}
