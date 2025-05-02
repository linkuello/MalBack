package com.ms.mal_back.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorites", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "ad_id"}))
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  // Unique identifier for each favorite entry

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Advertisement advertisement;
}
