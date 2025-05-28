package com.nonglam.open_server.domain.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpenerRepository extends JpaRepository<Opener, Long> {
  Optional<Opener> findByUsername(String username);

  Optional<Opener> findByEmail(String email);

  @Query(value = """
      SELECT u.* FROM user u
      JOIN  opener_follows f1 on u.id=f1.followed_id
      JOIN  opener_follows f2 on u.id=f2.follower_id
      WHERE f1.follower_id=:id
      AND f2.followed_id=:id
        """, nativeQuery = true)
  Page<Opener> findMutualFriends(@Param("id") Long id, Pageable pageable);
}
