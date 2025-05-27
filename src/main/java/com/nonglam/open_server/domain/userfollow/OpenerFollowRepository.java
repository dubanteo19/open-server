package com.nonglam.open_server.domain.userfollow;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nonglam.open_server.domain.user.Opener;

public interface OpenerFollowRepository extends JpaRepository<OpenerFollow, Long> {

  @Query("""
      SELECT of.followed FROM OpenerFollow of
      JOIN of.follower follower
      WHERE follower.id =:followerId
        """)
  Page<Opener> findAllByFollowerId(@Param("followerId") Long followerId, Pageable pageable);

  @Query("""
      SELECT of.follower FROM OpenerFollow of
      JOIN of.followed followed
      WHERE followed.id =:followedId
        """)
  Page<Opener> findAllByFollowedId(@Param("followedId") Long followedId, Pageable pageable);

  @Query("""
        SELECT of.followed.id FROM OpenerFollow of
        WHERE of.follower.id =:id
        AND of.followed.id IN :openerIds
      """)
  Set<Long> findFollowedIds(@Param("id") Long id, @Param("openerIds") List<Long> openerIds);

  @Query("""
        SELECT of  FROM OpenerFollow of
        WHERE of.follower.id =:id
        AND of.followed.id = :currentOpenerId
      """)
  Optional<OpenerFollow> findByFollowerIdAndFollowedId(@Param("id") Long id,
      @Param("currentOpenerId") Long currentOpenerId);

  boolean existsByFollowedIdAndFollowerId(Long id, Long id2);
}
