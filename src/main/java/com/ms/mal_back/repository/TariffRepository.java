package com.ms.mal_back.repository;

import com.ms.mal_back.entity.TariffEntry;
import com.ms.mal_back.entity.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntry, Long> {

    Optional<TariffEntry> findByPriorityAndDurationDays(Priority priority, int durationDays);

    List<TariffEntry> findAllByOrderByPriorityAscDurationDaysAsc();
}

