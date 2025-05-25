package com.nonglam.open_server.domain.postlike;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  Optional<PostLike> findByPostIdAndOpenerId(Long postId, Long openerId);

  @Query("SELECT pl.post.id FROM PostLike pl WHERE pl.opener.id =:openerId AND pl.post.id IN :postIds")
  Set<Long> findLikedPostIds(@Param("openerId") Long openerId, @Param("postIds") List<Long> postIds);

  List<PostLike> findAllByPostId(Long postId, Sort sort);

}
