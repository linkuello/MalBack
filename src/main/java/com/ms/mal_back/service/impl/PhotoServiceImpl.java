package com.ms.mal_back.service.impl;

import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Certification;
import com.ms.mal_back.entity.Photo;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.CertificationRepository;
import com.ms.mal_back.repository.PhotoRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.PhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Value("${photo.upload-dir}")
    private String uploadDir;

    private final PhotoRepository photoRepo;
    private final UserRepository userRepo;
    private final CertificationRepository certRepo;
    private final AdvertisementRepository adRepo;

    public PhotoServiceImpl(PhotoRepository photoRepo, UserRepository userRepo,
                            CertificationRepository certRepo, AdvertisementRepository adRepo) {
        this.photoRepo = photoRepo;
        this.userRepo = userRepo;
        this.certRepo = certRepo;
        this.adRepo = adRepo;
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Max file size is 5MB");
        }
        String type = file.getContentType();
        if (!List.of("image/jpeg", "image/png").contains(type)) {
            throw new IllegalArgumentException("Only JPG and PNG files are allowed");
        }
    }

    private Photo savePhotoToDisk(MultipartFile file) {
        validateFile(file);
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path savePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            Photo photo = new Photo();
            photo.setFileName(fileName);
            photo.setFilePath("/photos/" + fileName);
            photo.setMimeType(file.getContentType());
            photo.setUploadTime(LocalDateTime.now());

            return photo;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo", e);
        }
    }

    private void deletePhoto(Photo photo) {
        if (photo == null) return;
        try {
            Files.deleteIfExists(Paths.get(uploadDir, Paths.get(photo.getFilePath()).getFileName().toString()));
        } catch (IOException e) {
            // Optional: log error
        }
        photoRepo.delete(photo);
    }

    @Override
    public void replaceProfilePhoto(User user, MultipartFile file) {
        if (user.getPhoto() != null) {
            deletePhoto(user.getPhoto());
        }
        Photo newPhoto = savePhotoToDisk(file);
        newPhoto.setUser(user);
        user.setPhoto(photoRepo.save(newPhoto));
        userRepo.save(user);
    }

    @Override
    public void replaceCertificationPhoto(Certification cert, MultipartFile file) {
        if (cert.getPhoto() != null) {
            deletePhoto(cert.getPhoto());
        }
        Photo newPhoto = savePhotoToDisk(file);
        newPhoto.setCertification(cert);
        cert.setPhoto(photoRepo.save(newPhoto));
        certRepo.save(cert);
    }

    @Override
    public void replaceAdPhotos(Advertisement ad, List<MultipartFile> files) {
        List<Photo> existingPhotos = ad.getPhotos();

        if (existingPhotos != null && !existingPhotos.isEmpty()) {
            for (Photo photo : new ArrayList<>(existingPhotos)) {
                deletePhoto(photo); // delete file from disk if needed
            }
            existingPhotos.clear(); // 🔥 keep same collection instance
        }

        for (MultipartFile file : files) {
            Photo photo = savePhotoToDisk(file);
            photo.setAdvertisement(ad);
            photoRepo.save(photo);
            existingPhotos.add(photo); // ✅ use existing ad.getPhotos()
        }

        adRepo.save(ad); // optional, depends on your cascade setup
    }

    public void appendAdPhotos(Advertisement ad, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            Photo photo = savePhotoToDisk(file);
            photo.setAdvertisement(ad);
            photoRepo.save(photo);
            ad.getPhotos().add(photo);
        }

        adRepo.save(ad);
    }


}
