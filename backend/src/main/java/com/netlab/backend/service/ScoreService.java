package com.netlab.backend.service;

import com.netlab.backend.model.Score;
import com.netlab.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Score> getAllScores(){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database kemudian kembalikan hasilnya
        return scoreRepository.findAll();
        // hint: Panggil method yang sama seperti kode yang kalian buat di TP nomor 4
    }
    // Pre-CS
    public Optional<Score> getScoreByID(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }

    public List<Score> getRecentScores(){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database dengan urutan pembuatan terbaru kemudian kembalikan hasilnya
    return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getScoreAboveValue(Integer minValue){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database yang memiliki point di atas nilai tertentu
        return scoreRepository.findByPointGreaterThan(minValue);
        // gunakan minValue sebagai batas bawah nilai point
    }

    public List<Score> getLeaderboard(Integer limit) {
    // TODO: Gunakan scoreRepository untuk mencari Top Scores dan berikan parameter yang sesuai
        return  scoreRepository.findTopScores(limit);
}

    public void deleteScore(UUID scoreId) {
        // TODO:
        // 1. Cari score yang ingin dihapus menggunakan scoreRepository kemudian simpan score tersebut (hint: lihat caranya di getScoreById())
        // 2. Cek apakah score tersebut ditemukan atau tidak dengan `.orElseThrow(()-> new RuntimeException("Score dengan ID " + scoreId + " tidak ditemukan"));`
        // 3. Panggil delete() dari scoreRepository untuk menghapus score yang disimpan tadi
        Score score = scoreRepository.findById(scoreId)
                .orElseThrow(); new RuntimeException("Score dengan ID" + scoreId + " Tidak Ditemukan");
        scoreRepository.delete(score);


}
}