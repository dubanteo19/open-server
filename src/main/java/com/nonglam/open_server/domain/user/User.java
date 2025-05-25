package com.nonglam.open_server.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Data
public abstract class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;
  @Column(nullable = false, unique = true)
  protected String email;
  protected String username;
  protected String displayName;
  @Column(length = 400)
  protected String avatarUrl;
  @Enumerated(EnumType.STRING)
  Role role = Role.OPENER;
  protected String password;
  protected boolean blocked;
  @Column(nullable = false)
  protected boolean registeredWithGoogle = false;


}
