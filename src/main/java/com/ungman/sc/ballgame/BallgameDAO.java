package com.ungman.sc.ballgame;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BallgameDAO {
	
	@Autowired
    private BallgameRepository ballgameRepository;

    // 점수 저장
    public void saveScore(String playerName, int score) {
        // 새로운 점수 저장 (또는 기존 점수 업데이트)
        Ballgame ballgameScore = ballgameRepository.findByPlayerName(playerName);

        if (ballgameScore == null) {
            // 새로 플레이어 추가
            ballgameScore = new Ballgame();
            ballgameScore.setPlayerName(playerName);
            ballgameScore.setScore(score);
        } else {
            // 기존 점수와 비교하여 더 높은 점수일 경우 업데이트
            if (ballgameScore.getScore() < score) {
                ballgameScore.setScore(score);
            }
        }

        ballgameRepository.save(ballgameScore);
    }

    // 상위 5개 점수 조회
    public List<Ballgame> getTopScores() {
        return ballgameRepository.findTop5ByOrderByScoreDesc();
    }

}
