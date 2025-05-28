package com.nonglam.open_server.domain.chat.conversation;

import java.util.ArrayList;
import java.util.List;

import com.nonglam.open_server.domain.chat.message.Message;
import com.nonglam.open_server.domain.user.Opener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "conversationKey"))
public class Conversation {
  public Conversation(Opener opener1, Opener opener2, String key) {
    this.opener1 = opener1;
    this.opener2 = opener2;
    this.conversationKey = key;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Opener opener1;
  @ManyToOne
  private Opener opener2;
  private String conversationKey;
  @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
  @Builder.Default
  private List<Message> messages = new ArrayList<>();
}
