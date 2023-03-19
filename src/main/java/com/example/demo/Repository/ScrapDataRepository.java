package com.example.demo.Repository;

import com.example.demo.Domain.ScrapData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapDataRepository extends JpaRepository<ScrapData, Long> {
}