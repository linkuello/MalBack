package com.ms.mal_back.entity;

import com.ms.mal_back.entity.enums.Priority;
import com.ms.mal_back.entity.enums.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String animal;

    @Column(nullable = false)
    private String breed;
    private LocalDate age;

    @Enumerated(EnumType.STRING)
    private Region region;

    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    private LocalDate createdAt;
    private LocalDate lastModifiedAt;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.STANDARD;

    private int viewCount = 0;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.lastModifiedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDate.now();
    }

    private LocalDate priorityUntil;
}

