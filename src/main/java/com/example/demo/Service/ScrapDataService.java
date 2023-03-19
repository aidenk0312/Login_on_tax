package com.example.demo.Service;

import com.example.demo.Domain.ScrapData;
import com.example.demo.Repository.ScrapDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapDataService {
    @Autowired
    private ScrapDataRepository scrapDataRepository;

    public void saveScrapData(ScrapData scrapData) {
        scrapDataRepository.save(scrapData);
    }
}