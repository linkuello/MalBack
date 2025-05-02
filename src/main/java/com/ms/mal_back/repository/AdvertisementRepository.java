package com.ms.mal_back.repository;

import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findBySeller(User seller);
    List<Advertisement> findAllByIdIn(Set<Long> ids);

    @Query("SELECT DISTINCT a.animal FROM Advertisement a")
    List<String> findDistinctAnimals();

    @Query("SELECT DISTINCT a.breed FROM Advertisement a WHERE a.animal = ?1")
    List<String> findDistinctBreedsByAnimal(String animal);

}
