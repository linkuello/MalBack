package com.ms.mal_back.repository;

import com.ms.mal_back.entity.Certification;
import com.ms.mal_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    @Query("SELECT c FROM Certification c LEFT JOIN FETCH c.photo WHERE c.user = :user")
    List<Certification> findByUser(@Param("user") User user);
}