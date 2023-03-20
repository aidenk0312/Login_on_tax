package com.example.demo.Service;

import com.example.demo.Domain.ScrapData;
import com.example.demo.Repository.ScrapDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ScrapDataService {
    @Autowired
    private ScrapDataRepository scrapDataRepository;

    public void saveScrapData(ScrapData scrapData) {
        scrapDataRepository.save(scrapData);
    }

    public ScrapData getScrapDataByUserId(String userId) {
        try {
            return scrapDataRepository.findByUserId(userId).orElse(null);
        } catch (IncorrectResultSizeDataAccessException e) {
            System.err.println("중복된 ScrapData가 발견되었습니다. UserId: " + userId);
            return null;
        }
    }
}