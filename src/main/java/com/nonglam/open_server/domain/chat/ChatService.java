package com.nonglam.open_server.domain.chat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nonglam.open_server.domain.chat.conversation.Conversation;
import com.nonglam.open_server.domain.chat.conversation.ConversationMapper;
import com.nonglam.open_server.domain.chat.conversation.ConversationRepository;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationResponse;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationSummaryResponse;
import com.nonglam.open_server.domain.chat.eventlistener.event.MessageCreatedEvent;
import com.nonglam.open_server.domain.chat.message.Message;
import com.nonglam.open_server.domain.chat.message.MessageMapper;
import com.nonglam.open_server.domain.chat.message.MessageRepository;
import com.nonglam.open_server.domain.chat.message.dto.MessageCreateRequest;
import com.nonglam.open_server.domain.chat.message.dto.MessageResponse;
import com.nonglam.open_server.domain.notification.eventlistener.event.NotificationEvent;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final MessageRepository messageRepository;
  private final OpenerService openerService;
  private final ConversationRepository conversationRepository;
  private final ConversationMapper conversationMapper;
  private final MessageMapper messageMapper;
  private final ApplicationEventPublisher eventPublisher;

  private String generateConversationKey(Long openerId1, Long openerId2) {
    return Stream.of(openerId1, openerId2).sorted()
        .map(String::valueOf).collect(Collectors.joining("_"));
  }

  public ConversationSummaryResponse getOrCreateConversation(Long currentOpenerId, Long targetOpenerId) {
    String key = generateConversationKey(currentOpenerId, targetOpenerId);
    var conversation = conversationRepository.findByConversationKey(key)
        .orElseGet(() -> createConversation(currentOpenerId, targetOpenerId, key));
    return conversationMapper.toSummary(conversation, currentOpenerId);
  }

  public Conversation createConversation(Long currentOpenerId, Long targetOpenerId, String key) {
    Opener opener1 = openerService.findById(currentOpenerId);
    Opener opener2 = openerService.findById(targetOpenerId);
    List<Opener> sorted = Stream.of(opener1, opener2)
        .sorted(Comparator.comparing(Opener::getId)).toList();
    var conversation = new Conversation(sorted.get(0), sorted.get(1), key);
    return conversationRepository.save(conversation);
  }

  public Conversation findConversationById(Long id) {
    return conversationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("conversation not found"));
  }

  public long countUnseenConversations(Long currentOpenerId) {
    System.out.println(currentOpenerId);
    return conversationRepository.countUnseenConversations(currentOpenerId);
  }

  public List<ConversationSummaryResponse> getConversationSummaryList(Long currentOpenerId) {
    var conversations = conversationRepository.findAllByOpenerId(currentOpenerId);
    var sortedConversations = conversations.stream()
        .sorted(Comparator.comparing(
            conversation -> conversation.getLastMessage() != null
                ? conversation.getLastMessage().getCreatedAt()
                : null,
            Comparator.nullsLast(Comparator.reverseOrder())))
        .toList();
    return conversationMapper.toSummaryList(sortedConversations, currentOpenerId);
  }

  public ConversationResponse getConversationById(Long id, Long currentOpenerId) {
    var conversation = findConversationById(id);
    var messages = messageRepository.findAllByConversationId(id);
    return conversationMapper.toResponse(conversation, messages, currentOpenerId);
  }

  @Transactional
  public MessageResponse saveMessage(String username, MessageCreateRequest request) {
    var sender = openerService.findByUsername(username);
    var conversation = findConversationById(request.conversationId());
    var message = new Message(sender, conversation, request.content());
    var savedMessage = messageRepository.save(message);
    var notification = new NotificationEvent(request.receiver(), username + " has just sent you a message");
    eventPublisher.publishEvent(new MessageCreatedEvent(savedMessage.getId(), conversation.getId()));
    eventPublisher.publishEvent(notification);
    return messageMapper.toResponse(savedMessage);

  }

  public void markAsSeen(Long id, Long currentOpenerId) {
    var conversation = findConversationById(id);
    for (var message : conversation.getMessages()) {
      if (!message.isSeen() && message.getSender().getId() != currentOpenerId)
        message.markAsSeen();
    }
    conversationRepository.save(conversation);
  }
}
