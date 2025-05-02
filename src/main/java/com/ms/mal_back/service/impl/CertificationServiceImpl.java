package com.ms.mal_back.service.impl;
import com.ms.mal_back.dto.CertificationRequest;
import com.ms.mal_back.dto.CertificationResponse;
import com.ms.mal_back.entity.Certification;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.exception.ForbiddenAccessException;
import com.ms.mal_back.mapper.CertificationMapper;
import com.ms.mal_back.repository.CertificationRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.CertificationService;
import com.ms.mal_back.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final CertificationMapper certificationMapper;
    private final PhotoService photoService;

    @Override
    public CertificationResponse createCertification(Long userId, CertificationRequest request, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Certification cert = certificationMapper.toEntity(request, user);
        certificationRepository.save(cert);

        photoService.replaceCertificationPhoto(cert, file);

        return certificationMapper.toDto(cert);
    }

    @Override
    public void deleteCertification(Long certificationId, Long userId) {
        Certification cert = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certification not found"));

        if (!cert.getUser().getId().equals(userId)) {
            throw new ForbiddenAccessException("You are not allowed to delete this certification.");
        }

        certificationRepository.delete(cert);
    }

    @Override
    public List<CertificationResponse> getCertificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return certificationMapper.toDtos(user.getCertifications());
    }
}
