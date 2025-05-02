package com.ms.mal_back.dto;

import com.ms.mal_back.entity.enums.CertificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificationResponse {
    private Long id;
    private String name;
    private CertificationType type;
    private String photoUrl;
}
