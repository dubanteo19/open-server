package com.nonglam.open_server.domain.post;

import com.nonglam.open_server.domain.comment.dto.requset.CommentPayload;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findAllByDeleted(boolean b, Pageable pageable);

  Page<Post> findByAuthor_UsernameAndDeleted(String authorUsername, boolean deleted, Pageable pageable);

  Optional<Post> findByIdAndDeleted(Long id, boolean deleted);

  @Transactional
  @Modifying
  @Query("UPDATE Post p SET p.viewCount = p.viewCount +1 WHERE p.id = :postId")
  void incrementViewCount(@Param("postId") Long postId);

  @Transactional
  @Modifying
  @Query("UPDATE Post p SET p.commentCount = p.commentCount +1 WHERE p.id = :postId")
  void incrementCommentCount(@Param("postId") Long postId);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE Post p SET p.likeCount = p.likeCount +1 WHERE p.id = :postId")
  void incrementLikeCount(@Param("postId") Long postId);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE Post p SET p.likeCount = p.likeCount -1 WHERE p.id = :postId")
  void decrementLikeCount(@Param("postId") Long postId);

  List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

  @Query("SELECT p FROM Post p WHERE p.deleted=false ORDER BY p.id DESC")
  List<Post> findTopNOrderByIdDesc(Pageable pageable);

  @Query("SELECT p FROM Post p WHERE p.id < :after ORDER BY p.id DESC")
  List<Post> findTopNByIdLessThanOrderThanDesc(@Param("after") Long after, Pageable pageable);

}
