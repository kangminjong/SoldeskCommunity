package com.ungman.sc.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.ungman.sc.user.User;
import com.ungman.sc.user.UserRepository;

import jakarta.annotation.PostConstruct;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final UserRepository userRepository;

    @Value("${spring.auth.code.expiration-millis}")
    private long expirationMillis;

    @Value("${spring.auth.email.pattern}")
    private String emailPatternString;
    
    
    private Pattern EMAIL_PATTERN;

    @PostConstruct
    public void init() {
        EMAIL_PATTERN = Pattern.compile(emailPatternString);
    }

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    // 회원가입을 위한 인증 코드 요청  
    // 이미 가입된 이메일인 경우 인증코드 전송 안함.
    @PostMapping("/send-code")
    public ResponseEntity<?> sendVerificationCode(@RequestParam("email") String email) {
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            logger.warn("잘못된 이메일 형식: {}", email);
            return ResponseEntity.badRequest().body(Map.of("error", "잘못된 이메일 형식입니다."));
        }

        // 이미 가입된 이메일 확인
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            logger.warn("이미 가입된 이메일: {}", email);
            return ResponseEntity.badRequest().body(Map.of("error", "이미 가입된 이메일입니다."));
        }

        // 인증 코드 전송
        String responseMessage = authService.sendJoinVerificationCode(email);
        if (responseMessage.contains("인증 코드가 이메일로 전송되었습니다.")) {
            logger.info("회원가입용 인증 코드 전송 성공: {}", email);
            return ResponseEntity.ok(Map.of("message", responseMessage));
        } else {
            logger.error("이메일 전송 실패: {}", email);
            return ResponseEntity.status(500).body(Map.of("error", responseMessage));
        }
    }

	 // 비밀번호 찾기를 위한 인증 코드 요청  
	 // 가입된 이메일에 대해서만 인증코드 전송.
	 @PostMapping("/send-reset-code")
	 public ResponseEntity<?> sendResetPasswordCode(@RequestParam("email") String email) {
	     if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
	         logger.warn("잘못된 이메일 형식: {}", email);
	         return ResponseEntity.badRequest().body(Map.of("error", "잘못된 이메일 형식입니다."));
	     }
	
	     // 가입된 이메일 확인
	     Optional<User> existingUser = userRepository.findByEmail(email);
	     if (existingUser.isEmpty()) {
	         logger.warn("가입되지 않은 이메일: {}", email);
	         return ResponseEntity.badRequest().body(Map.of("error", "가입되지 않은 이메일입니다."));
	     }
	
	     // 인증 코드 전송
	     String responseMessage = authService.sendResetVerificationCode(email);
	     if (responseMessage.contains("인증 코드가 이메일로 전송되었습니다.")) {
	         logger.info("비밀번호 찾기용 인증 코드 전송 성공: {}", email);
	         return ResponseEntity.ok(Map.of("message", responseMessage));
	     } else {
	         logger.error("이메일 전송 실패: {}", email);
	         return ResponseEntity.status(500).body(Map.of("error", responseMessage));
	     }
	 }

    // 인증 코드 검증  
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestParam("email") String email,
                                        @RequestParam("code") String code) {
        if (email == null || email.isBlank() || code == null || code.isBlank()) {
            logger.warn("이메일 또는 코드가 입력되지 않음: email={}, code={}", email, code);
            return ResponseEntity.badRequest().body(Map.of("error", "이메일과 인증 코드를 입력하세요."));
        }
        
        // 인증 코드 검증
        return authService.verifyCode(email, code);
    }
}
