package com.ms.mal_back.repository;

import com.ms.mal_back.dto.admin.UserResponse;
import com.ms.mal_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query("""
        SELECT new com.ms.mal_back.dto.admin.UserResponse(
            u.id, u.username, u.email, u.phone, u.role.name
        )
        FROM User u
        WHERE (:name IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:phone IS NULL OR u.phone LIKE CONCAT('%', :phone, '%'))
          AND (:role IS NULL OR u.role.name = :role)
        ORDER BY u.id DESC
    """)
    List<UserResponse> adminSearch(String name, String phone, String role);
}

