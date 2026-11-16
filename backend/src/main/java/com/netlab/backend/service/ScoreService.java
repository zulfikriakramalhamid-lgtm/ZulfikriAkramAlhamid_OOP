
package com.netlab.backend.service;

import com.netlab.backend.model.Score;
import com.netlab.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    // Pre-CS
    public Score createScore(Score score) {
        return scoreRepository.save(score);
    }

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    // Pre-CS
    public Optional<Score> getScoreByID(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }

    public List<Score> getRecentScores() {
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getScoreAboveValue(Integer minValue) {
        return scoreRepository.findByPointGreaterThan(minValue);
    }

    public List<Score> getLeaderboard(Integer limit) {
        return scoreRepository.findTopScores(PageRequest.of(0, limit));
    }

    public void deleteScore(UUID scoreId) {
        Score score = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new RuntimeException(
                        "Score dengan ID " + scoreId + " tidak ditemukan"));
        scoreRepository.delete(score);
    }
}
