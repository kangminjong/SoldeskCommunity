package com.ungman.sc.ballgame;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BallgameRepository extends JpaRepository<Ballgame, Integer> {

    Ballgame findByPlayerName(String playerName);

    List<Ballgame> findTop5ByOrderByScoreDesc();
}
