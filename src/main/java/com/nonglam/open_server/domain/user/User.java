package com.nonglam.open_server.domain.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Data
public abstract class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true)
  private String email;
  private String username;
  private String displayName;
  @Column(length = 400)
  private String avatarUrl;
  @Enumerated(EnumType.STRING)
  private Role role = Role.OPENER;
  private String password;
  @Column(nullable = false)
  private boolean registeredWithGoogle = false;

}
