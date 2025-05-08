package com.ms.mal_back.entity;

import com.ms.mal_back.entity.enums.Priority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
public class TariffEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private int durationDays;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;
}

