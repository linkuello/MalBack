package com.ms.mal_back.repository;

import com.ms.mal_back.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndAdvertisementId(Long userId, Long advertisementId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId AND f.advertisement.id = :adId")
    void deleteByUserIdAndAdId(@Param("userId") Long userId, @Param("adId") Long adId);
    List<Favorite> findByUserId(Long userId);
}
