package com.nonglam.open_server.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpenerRepository extends JpaRepository<Opener, Long> {
    Optional<Opener> findByUsername(String username);

    Optional<Opener> findByEmail(String email);
}
