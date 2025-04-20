package com.nonglam.open_server.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByAuthor_Username(String username,
                                     Pageable pageable);

    Page<Post> findAllByDeleted(boolean b, Pageable pageable);

    Page<Post> findByAuthor_UsernameAndDeleted(String authorUsername, boolean deleted, Pageable pageable);
}
