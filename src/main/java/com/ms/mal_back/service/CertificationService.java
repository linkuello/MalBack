package com.ms.mal_back.service;

import com.ms.mal_back.dto.CertificationRequest;
import com.ms.mal_back.dto.CertificationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CertificationService {
    CertificationResponse createCertification(Long userId, CertificationRequest request, MultipartFile file);
    void deleteCertification(Long certificationId, Long userId);
    List<CertificationResponse> getCertificationsByUser(Long userId);
}
