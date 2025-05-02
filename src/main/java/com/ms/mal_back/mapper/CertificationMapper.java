package com.ms.mal_back.mapper;

import com.ms.mal_back.dto.CertificationRequest;
import com.ms.mal_back.dto.CertificationResponse;
import com.ms.mal_back.entity.Certification;
import com.ms.mal_back.entity.User;

import java.util.List;

public interface CertificationMapper {
    Certification toEntity(CertificationRequest request, User user);
    CertificationResponse toDto(Certification cert);
    List<CertificationResponse> toDtos(List<Certification> certs);
}

