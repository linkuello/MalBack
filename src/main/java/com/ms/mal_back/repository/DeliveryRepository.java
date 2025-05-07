package com.ms.mal_back.repository;

import com.ms.mal_back.entity.Delivery;
import com.ms.mal_back.entity.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByBuyerId(Long buyerId);

    List<Delivery> findBySellerId(Long sellerId);

    List<Delivery> findByStatus(DeliveryStatus status);

    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}

