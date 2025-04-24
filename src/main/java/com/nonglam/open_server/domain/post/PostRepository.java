package com.nonglam.open_server.domain.post;

import com.nonglam.open_server.domain.comment.dto.requset.CommentPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {


    Page<Post> findAllByDeleted(boolean b, Pageable pageable);

    Page<Post> findByAuthor_UsernameAndDeleted(String authorUsername, boolean deleted, Pageable pageable);

    Optional<Post> findByIdAndDeleted(Long id, boolean deleted);

}
