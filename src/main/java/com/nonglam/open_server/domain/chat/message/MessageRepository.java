package com.nonglam.open_server.domain.chat.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findAllByConversationId(Long id);

}
