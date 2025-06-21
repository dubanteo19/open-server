package com.nonglam.open_server.domain.notification;

import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Notification extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Opener opener;
  @Column(nullable = false, length = 200)
  private String content;
  private boolean isRead = false;

  public Notification(String content, Opener opener) {
    this.content = content;
    this.opener = opener;
  }

  public void markAsRead() {
    this.isRead = true;
  }
}
