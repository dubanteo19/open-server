package com.nonglam.open_server.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @Column(nullable = false, unique = true)
  String email;
  String username;
  String displayName;
  @Column(length = 400)
  String avatarUrl;
  @Enumerated(EnumType.STRING)
  Role role = Role.OPENER;
  String password;
  boolean blocked;
  @Column(nullable = false)
  boolean registeredWithGoogle = false;

}
