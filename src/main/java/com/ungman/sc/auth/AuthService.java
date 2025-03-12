package com.ungman.sc.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ungman.sc.user.User;
import com.ungman.sc.user.UserRepository;
import org.springframework.mail.MailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;

    @Value("${spring.auth.code.expiration-millis}")
    private long expirationMillis;

    public AuthService(EmailService emailService, 
                       VerificationCodeRepository verificationCodeRepository, 
                       UserRepository userRepository) {
        this.emailService = emailService;
        this.verificationCodeRepository = verificationCodeRepository;
        this.userRepository = userRepository;
    }

    // 회원가입 인증 코드 전송
    @Transactional
    public String sendJoinVerificationCode(String email) {
        // 이미 가입된 이메일 체크
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            logger.warn("이미 가입된 이메일로 인증 코드 요청: {}", email);
            return "이미 가입된 이메일입니다.";
        }
        return sendVerificationCode(email, "회원가입");
    }

    // 비밀번호 찾기 인증 코드 전송
    @Transactional
    public String sendResetVerificationCode(String email) {
        // 가입되지 않은 이메일 체크
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            logger.warn("가입되지 않은 이메일로 인증 코드 요청: {}", email);
            return "가입되지 않은 이메일입니다.";  // 가입되지 않은 이메일인 경우
        }
        return sendVerificationCode(email, "비밀번호 찾기");
    }


    // 인증 코드 전송 (공용 로직)
    private String sendVerificationCode(String email, String purpose) {
        // 기존 인증 코드 삭제
        verificationCodeRepository.deleteByEmail(email);

        // 새로운 인증 코드 생성
        String code = emailService.generateVerificationCode();
        VerificationCode verificationCode = new VerificationCode(
                email, code, LocalDateTime.now().plus(Duration.ofMillis(expirationMillis))
        );
        verificationCodeRepository.save(verificationCode);

        // 이메일 전송 처리
        try {
            emailService.sendVerificationEmail(email, code);
            logger.info("{}용 인증 코드 전송 완료. 이메일: {}", purpose, email);
            return "인증 코드가 이메일로 전송되었습니다.";
        } catch (MailException e) {
            logger.error("{}용 이메일 전송 실패. 이메일: {}, 오류: {}", purpose, email, e.getMessage(), e);
            return "이메일 전송 실패";
        } catch (RuntimeException e) {
            logger.error("{}용 이메일 전송 중 오류 발생. 이메일: {}, 오류: {}", purpose, email, e.getMessage(), e);
            return "이메일 전송 중 오류 발생";
        }
    }

    // 인증 코드 검증
    public ResponseEntity<?> verifyCode(String email, String code) {
        if (email == null || email.isBlank() || code == null || code.isBlank()) {
            logger.warn("이메일 또는 인증 코드가 비어있음: email={}, code={}", email, code);
            return ResponseEntity.badRequest().body(Map.of("message", "이메일과 인증 코드를 입력하세요."));
        }

        // 인증 코드 검증
        Optional<VerificationCode> storedCode = 
                verificationCodeRepository.findValidCode(email, code, LocalDateTime.now());
        if (storedCode.isPresent()) {
            logger.info("인증 코드 검증 성공. 이메일: {}", email);
            return ResponseEntity.ok(Map.of("message", "인증 성공"));
        } else {
            logger.warn("인증 코드 검증 실패. 이메일: {}, 코드: {}", email, code);
            return ResponseEntity.status(400)
                                 .body(Map.of("message", "인증 코드가 올바르지 않거나 만료되었습니다."));
        }
    }
}
