package com.nonglam.open_server.domain.chat.conversation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

  Optional<Conversation> findByConversationKey(String key);

  @Query("""
      SELECT c FROM Conversation c
      WHERE c.opener1.id=:id
      OR c.opener2.id=:id
        """)
  List<Conversation> findAllByOpenerId(@Param("id") Long id);

}
