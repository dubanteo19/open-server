package com.nonglam.open_server.domain.postbookmark;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
  Optional<PostBookmark> findByPostIdAndOpenerId(Long postId, Long openerId);

  @Query("SELECT pb.post.id FROM PostBookmark pb WHERE pb.opener.id =:openerId AND pb.post.id IN :postIds")
  Set<Long> findBookmarkedPostIds(@Param("openerId") Long openerId, @Param("postIds") List<Long> postIds);
}
