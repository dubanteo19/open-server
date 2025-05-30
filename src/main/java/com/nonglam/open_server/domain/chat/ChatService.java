package com.nonglam.open_server.domain.chat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nonglam.open_server.domain.chat.conversation.Conversation;
import com.nonglam.open_server.domain.chat.conversation.ConversationMapper;
import com.nonglam.open_server.domain.chat.conversation.ConversationRepository;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationResponse;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationSummaryResponse;
import com.nonglam.open_server.domain.chat.message.Message;
import com.nonglam.open_server.domain.chat.message.MessageMapper;
import com.nonglam.open_server.domain.chat.message.MessageRepository;
import com.nonglam.open_server.domain.chat.message.dto.MessageCreateRequest;
import com.nonglam.open_server.domain.chat.message.dto.MessageResponse;
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

  public List<ConversationSummaryResponse> getConversationSummaryList(Long currentOpenerId) {
    var conversations = conversationRepository.findAllByOpenerId(currentOpenerId);
    return conversationMapper.toSummaryList(conversations, currentOpenerId);
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
    return messageMapper.toResponse(savedMessage);

  }
}
