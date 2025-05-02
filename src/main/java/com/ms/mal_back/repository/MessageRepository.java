package com.ms.mal_back.repository;

import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatAndSenderIdNotAndIsReadFalse(Chat chat, Long senderId);
}