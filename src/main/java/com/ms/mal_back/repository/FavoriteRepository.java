package com.ms.mal_back.repository;

import com.ms.mal_back.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndAdvertisementId(Long userId, Long advertisementId);
    void deleteByUserIdAndAdvertisementId(Long userId, Long advertisementId);
    List<Favorite> findByUserId(Long userId);
}
