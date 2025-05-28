package com.nonglam.open_server.domain.chat.message;

import com.nonglam.open_server.domain.chat.conversation.Conversation;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.shared.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message extends Auditable {
  public Message(Opener sender, Conversation conversation, String content) {
    this.sender = sender;
    this.conversation = conversation;
    this.content = content;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Opener sender;
  @ManyToOne
  private Conversation conversation;
  @Column(nullable = false, length = 400)
  private String content;
  @Builder.Default
  private boolean seen = false;
}
