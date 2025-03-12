package com.ungman.sc.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.internet.MimeMessage;

import java.util.Random;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.auth.code.expiration-millis}")
    private long expirationMillis;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 인증 코드 생성
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // 이메일 전송 (MessagingException을 RuntimeException으로 변환)
    public void sendVerificationEmail(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("SoldeskCommunity 이메일 인증 코드");
            helper.setText("인증 코드: " + code + "\n이 코드는 " + (expirationMillis / 60000) + "분 동안 유효합니다.");

            logger.info("이메일 전송 시작: {}", toEmail);
            mailSender.send(message);
            logger.info("이메일 전송 성공: {}", toEmail);

        } catch (MailException | jakarta.mail.MessagingException e) {
            logger.error("이메일 전송 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이메일 전송 중 오류 발생", e);  
        }
    }

}
