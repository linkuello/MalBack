package com.ms.mal_back.service;

import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Certification;
import com.ms.mal_back.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {
    public void replaceProfilePhoto(User user, MultipartFile file);
    public void replaceCertificationPhoto(Certification cert, MultipartFile file);
    public void replaceAdPhotos(Advertisement ad, List<MultipartFile> files);
    public void appendAdPhotos(Advertisement ad, List<MultipartFile> files);
}
