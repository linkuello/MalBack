package com.ms.mal_back.entity;

import com.ms.mal_back.entity.enums.Priority;
import com.ms.mal_back.entity.enums.ReceiptStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="receipts")
public class PaymentReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Advertisement advertisement;

    @Enumerated(EnumType.STRING)
    private Priority requestedPriority;

    private int durationDays;

    @Enumerated(EnumType.STRING)
    private ReceiptStatus status = ReceiptStatus.NEW;

    private LocalDateTime submittedAt;

    @OneToOne(mappedBy = "paymentReceipt", cascade = CascadeType.ALL)
    private Photo receiptPhoto;
}
