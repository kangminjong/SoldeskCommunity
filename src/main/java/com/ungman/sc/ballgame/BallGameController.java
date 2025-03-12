package com.ungman.sc.ballgame;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BallGameController {
	
	@Autowired
	private BallgameDAO ballDAO;

	@GetMapping("/ballgame.go")
    public String showBallGame(HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
        return "ballgame/ballGame"; 
    }
	
	// 점수 저장 처리 (AJAX 호출 시 사용)
    @PostMapping("/saveScore")
    public String saveScore(@RequestParam String playerName, @RequestParam int score) {
    	ballDAO.saveScore(playerName, score);
        return "ballgame/ballGame";  // 게임 페이지로 다시 리디렉션
    }
    
    @PostMapping("/checkNameDuplicate")
    public ResponseEntity<Boolean> checkNameDuplicate(@RequestParam String playerName) {
        List<Ballgame> existingScores = ballDAO.getTopScores(); // DB에서 모든 점수 가져오기
        boolean isDuplicate = existingScores.stream().anyMatch(score -> score.getPlayerName().equals(playerName));
        return ResponseEntity.ok(isDuplicate);
    }
    
    @GetMapping("/topScores")
    @ResponseBody
    public List<Ballgame> getTopScores() {
        return ballDAO.getTopScores(); // 서비스에서 점수 가져오기
    }
}
