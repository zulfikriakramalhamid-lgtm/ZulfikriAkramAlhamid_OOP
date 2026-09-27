package com.netlab.backend.service;

import com.netlab.backend.model.Score;
import com.netlab.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    // Soal 5
    public Score createScore(Score score) {
        return scoreRepository.save(score);
    }

    // Soal 6
    public Optional<Score> getScoreByID(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }
}