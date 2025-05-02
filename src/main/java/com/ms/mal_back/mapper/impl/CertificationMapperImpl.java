package com.ms.mal_back.mapper.impl;

import com.ms.mal_back.config.UrlBuilder;
import com.ms.mal_back.dto.CertificationRequest;
import com.ms.mal_back.dto.CertificationResponse;
import com.ms.mal_back.entity.Certification;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.CertificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CertificationMapperImpl implements CertificationMapper {

    private final UrlBuilder urlBuilder;

    @Override
    public Certification toEntity(CertificationRequest request, User user) {
        Certification cert = new Certification();
        cert.setName(request.getName());
        cert.setType(request.getType());
        cert.setUser(user);
        return cert;
    }

    @Override
    public CertificationResponse toDto(Certification cert) {
        String photoUrl = cert.getPhoto() != null
                ? urlBuilder.buildFullPhotoUrl(cert.getPhoto().getFilePath())
                : null;

        return new CertificationResponse(
                cert.getId(),
                cert.getName(),
                cert.getType(),
                photoUrl
        );
    }

    @Override
    public List<CertificationResponse> toDtos(List<Certification> certs) {
        return certs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}


