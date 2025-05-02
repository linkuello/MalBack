package com.ms.mal_back.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "chats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ad_id", "seller_id", "customer_id"}),
        indexes = {
                @Index(columnList = "seller_id"),
                @Index(columnList = "customer_id")
        }
)

public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // ⬅️ Required!
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Advertisement advertisement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sentAt ASC") // ✅ ensures messages are always ordered
    private List<Message> messages = new ArrayList<>();

    private LocalDateTime lastMessageTime;
}

