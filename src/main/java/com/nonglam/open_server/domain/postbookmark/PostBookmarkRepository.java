package com.nonglam.open_server.domain.postbookmark;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nonglam.open_server.domain.post.Post;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
  Optional<PostBookmark> findByPostIdAndOpenerId(Long postId, Long openerId);

  @Query("SELECT pb.post.id FROM PostBookmark pb WHERE pb.opener.id =:openerId AND pb.post.id IN :postIds")
  Set<Long> findBookmarkedPostIds(@Param("openerId") Long openerId, @Param("postIds") List<Long> postIds);

  @Query("""
      SELECT p FROM PostBookmark pb
      JOIN pb.post p
      WHERE pb.opener.id =:openerId
      AND (:after IS NULL OR pb.id < :after)
        """)
  List<Post> findBookmarkedPostsByOpenerId(@Param("openerId") Long openerId, @Param("after") Long after,
      Pageable pageable);
}
