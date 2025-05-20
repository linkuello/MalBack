package com.ms.mal_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;
    private String mimeType;
    private LocalDateTime uploadTime;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(length = Integer.MAX_VALUE)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id", nullable = true)
    private Advertisement advertisement;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id", nullable = true)
    private Certification certification;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_receipt_id")
    private PaymentReceipt paymentReceipt;
}


