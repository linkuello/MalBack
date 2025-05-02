package com.ms.mal_back.entity;

import com.ms.mal_back.entity.enums.CertificationType;
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
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Human-readable title (e.g. "Veterinary License")
    private String name;

    // Type for filtering/validation logic
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificationType type;

    // Photo of the document (uploaded file)
    @OneToOne(mappedBy = "certification", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Photo photo;

    // Owner of the certification
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
