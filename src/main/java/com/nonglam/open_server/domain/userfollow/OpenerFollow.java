package com.nonglam.open_server.domain.userfollow;

import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "opener_follows", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "follower_id", "followed_id" })
})
@EqualsAndHashCode(callSuper = false)
public class OpenerFollow extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @ManyToOne
  @JoinColumn(name = "follower_id", nullable = false)
  Opener follower;
  @ManyToOne
  @JoinColumn(name = "followed_id", nullable = false)
  Opener followed;
}
