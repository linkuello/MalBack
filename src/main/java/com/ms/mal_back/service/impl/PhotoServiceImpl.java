package com.ms.mal_back.service.impl;

import com.ms.mal_back.entity.*;
import com.ms.mal_back.repository.*;
import com.ms.mal_back.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepo;
    private final UserRepository userRepo;
    private final CertificationRepository certRepo;
    private final AdvertisementRepository adRepo;
    private final PaymentReceiptRepository paymentReceiptRepo;

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Max file size is 5MB");
        }
        String type = file.getContentType();
        if (!List.of("image/jpeg", "image/png", "image/pdf").contains(type)) {
            throw new IllegalArgumentException("Only JPG, PNG, and PDF files are allowed");
        }
    }

    private Photo savePhotoToDatabase(MultipartFile file) {
        validateFile(file);
        try {
            Photo photo = new Photo();
            photo.setFileName(UUID.randomUUID() + "_" + file.getOriginalFilename());
            photo.setMimeType(file.getContentType());
            photo.setUploadTime(LocalDateTime.now());
            photo.setData(file.getBytes());
            return photo;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo to database", e);
        }
    }

    private void deletePhoto(Photo photo) {
        if (photo != null) {
            photoRepo.delete(photo);
        }
    }

    @Override
    public void replaceProfilePhoto(User user, MultipartFile file) {
        if (user.getPhoto() != null) {
            deletePhoto(user.getPhoto());
        }
        Photo newPhoto = savePhotoToDatabase(file);
        newPhoto.setUser(user);
        user.setPhoto(photoRepo.save(newPhoto));
        userRepo.save(user);
    }

    @Override
    public void replaceCertificationPhoto(Certification cert, MultipartFile file) {
        if (cert.getPhoto() != null) {
            deletePhoto(cert.getPhoto());
        }
        Photo newPhoto = savePhotoToDatabase(file);
        newPhoto.setCertification(cert);
        cert.setPhoto(photoRepo.save(newPhoto));
        certRepo.save(cert);
    }

    @Override
    public void replaceAdPhotos(Advertisement ad, List<MultipartFile> files) {
        List<Photo> existingPhotos = ad.getPhotos();

        if (existingPhotos != null && !existingPhotos.isEmpty()) {
            for (Photo photo : new ArrayList<>(existingPhotos)) {
                deletePhoto(photo);
            }
            existingPhotos.clear(); // keep reference to the same list
        }

        for (MultipartFile file : files) {
            Photo photo = savePhotoToDatabase(file);
            photo.setAdvertisement(ad);
            photoRepo.save(photo);
            existingPhotos.add(photo);
        }

        adRepo.save(ad); // ensure changes are persisted
    }

    @Override
    public void appendAdPhotos(Advertisement ad, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            Photo photo = savePhotoToDatabase(file);
            photo.setAdvertisement(ad);
            photoRepo.save(photo);
            ad.getPhotos().add(photo);
        }

        adRepo.save(ad);
    }

    @Override
    public void saveReceiptPhoto(PaymentReceipt receipt, MultipartFile file) {
        if (receipt.getReceiptPhoto() != null) {
            deletePhoto(receipt.getReceiptPhoto());
        }

        Photo newPhoto = savePhotoToDatabase(file);
        newPhoto.setPaymentReceipt(receipt);
        receipt.setReceiptPhoto(photoRepo.save(newPhoto));
        paymentReceiptRepo.save(receipt);
    }
}
