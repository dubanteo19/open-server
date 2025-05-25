package com.nonglam.open_server.domain.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

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

  @Query("SELECT p FROM Post p WHERE p.deleted=false ")
  List<Post> findTopN(Pageable pageable);

  @Query("""
      SELECT p FROM Post p WHERE p.deleted=false AND (:after IS NULL OR p.id < :after)
      """)
  List<Post> findTopNByIdLessThan(@Param("after") Long after, Pageable pageable);

  @Query("SELECT p FROM Post p WHERE p.deleted=false AND p.author.username=:username ")
  List<Post> findByAuthor(@Param("username") String username, Pageable pagerequest);

  @Query("""
      SELECT p FROM Post p
      WHERE p.deleted=false
      AND p.author.username=:username
      AND (:after IS NULL OR  p.id < :after)
      """)
  List<Post> findByAuthorAndIdLessThan(@Param("username") String username, @Param("after") Long after,
      Pageable pagerequest);

  List<Post> findByAuthorIdOrderByCreatedAtDesc(Long openerId, PageRequest of);

}
