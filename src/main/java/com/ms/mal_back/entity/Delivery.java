package com.ms.mal_back.entity;

import com.ms.mal_back.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Long adId;
    Long buyerId;
    Long sellerId;

    String buyerName;
    String buyerPhone;
    String buyerAddress;

    String sellerName;
    String sellerPhone;
    String sellerAddress;

    DeliveryStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}