package com.ms.mal_back.repository;

import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Chat;
import com.ms.mal_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByAdvertisementAndSellerAndCustomer(Advertisement advertisement, User seller, User customer);
    @Query("SELECT c FROM Chat c LEFT JOIN FETCH c.messages WHERE c.id = :chatId")
    Optional<Chat> findByIdWithMessages(@Param("chatId") Long chatId);
    @Query("""
    SELECT c FROM Chat c
    JOIN FETCH c.advertisement a
    LEFT JOIN FETCH a.photos
    WHERE c.seller.id = :userId OR c.customer.id = :userId
""")
    List<Chat> findAllByUserIdWithAd(@Param("userId") Long userId);
}