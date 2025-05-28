package com.nonglam.open_server.domain.chat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.chat.conversation.Conversation;
import com.nonglam.open_server.domain.chat.conversation.ConversationMapper;
import com.nonglam.open_server.domain.chat.conversation.ConversationRepository;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationResponse;
import com.nonglam.open_server.domain.chat.message.Message;
import com.nonglam.open_server.domain.chat.message.MessageMapper;
import com.nonglam.open_server.domain.chat.message.MessageRepository;
import com.nonglam.open_server.domain.chat.message.dto.MessageResponse;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.domain.user.OpenerService;
import com.nonglam.open_server.exception.ResourceNotFoundException;
import com.nonglam.open_server.shared.PagedResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final MessageRepository messageRepository;
  private final OpenerService openerService;
  private final ConversationRepository conversationRepository;
  private final ConversationMapper conversationMapper;
  private final MessageMapper messageMapper;

  public ConversationResponse createConversation(Long currentOpenerId, Long targetOpenerId) {
    String key = generateConversationKey(currentOpenerId, targetOpenerId);
    var conversation = createConversation(currentOpenerId, targetOpenerId, key);
    return conversationMapper.toResponse(conversation, currentOpenerId);
  }

  public Conversation findById(Long id) {
    return conversationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("conversation not found"));
  }

  public Conversation getConversation(Long currentOpenerId, Long targetOpenerId) {
    String key = generateConversationKey(currentOpenerId, targetOpenerId);
    return conversationRepository.findByConversationKey(key)
        .orElseThrow(() -> new ResourceNotFoundException("conversation not found"));
  }

  public ConversationResponse getConversationResponse(Long currentOpenerId, Long targetOpenerId) {
    var conversation = getConversation(currentOpenerId, targetOpenerId);
    return conversationMapper.toResponse(conversation, currentOpenerId);
  }

  public MessageResponse sendMessage(Long senderId, Long receiverId, String content) {
    var conversation = getConversation(senderId, receiverId);
    var sender = openerService.findById(senderId);
    var message = new Message(sender, conversation, content);
    var savedMessage = messageRepository.save(message);
    return messageMapper.toResponse(savedMessage);
  }

  public PagedResponse<MessageResponse> getMessagesByConversationKey(String key) {
    return null;
  }

  private Conversation createConversation(Long openerId1, Long openerId2, String key) {
    Opener opener1 = openerService.findById(openerId1);
    Opener opener2 = openerService.findById(openerId2);
    List<Opener> sorted = Stream.of(opener1, opener2)
        .sorted(Comparator.comparing(Opener::getId)).toList();
    var conversation = new Conversation(sorted.get(0), sorted.get(1), key);
    return conversationRepository.save(conversation);
  }

  private String generateConversationKey(Long openerId1, Long openerId2) {
    return Stream.of(openerId1, openerId2).sorted()
        .map(String::valueOf).collect(Collectors.joining("_"));
  }

  public List<ConversationResponse> getConversations(Long currentOpenerId) {
    var conversations = conversationRepository.findAllByOpenerId(currentOpenerId);
    return conversationMapper.toResponseList(conversations, currentOpenerId);
  }

  public ConversationResponse getConversationById(Long id, Long currentOpenerId) {
    var conversation = findById(id);
    return conversationMapper.toResponse(conversation, currentOpenerId);
  }

  public List<MessageResponse> getMessagesByConversationId(Long id, Long currentOpenerId) {
    var messageList = messageRepository.findAllByConversationId(id);
    return messageMapper.toResponseList(messageList);
  }
}
