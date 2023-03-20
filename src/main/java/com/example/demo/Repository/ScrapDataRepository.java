package com.example.demo.Repository;

import com.example.demo.Domain.ScrapData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapDataRepository extends JpaRepository<ScrapData, Long> {
    Optional<ScrapData> findByUserId(String userId);

}