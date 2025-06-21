package com.nonglam.open_server.domain.chat.eventlistener.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.nonglam.open_server.domain.chat.conversation.ConversationRepository;
import com.nonglam.open_server.domain.chat.eventlistener.event.MessageCreatedEvent;
import com.nonglam.open_server.domain.chat.message.MessageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageCreatedListener {
  private final ConversationRepository conversationRepository;
  private final MessageRepository messageRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleMessageCreated(MessageCreatedEvent event) {
    var conversation = conversationRepository
        .findById(event.conversationId()).get();
    var message = messageRepository.findById(event.messageId()).get();
    conversation.setLastMessage(message);
    conversationRepository.save(conversation);
  }
}
