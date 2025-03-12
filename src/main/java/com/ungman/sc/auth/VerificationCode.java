package com.ungman.sc.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "verification_code_seq", sequenceName = "VERIFICATION_CODE_SEQ", allocationSize = 1)
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_code_seq")
    @Column(name = "VERIFICATION_CODE_ID")
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    // 생성자 (LocalDateTime 직접 설정)
    public VerificationCode(String email, String code, LocalDateTime expiryTime) {
        this.email = email;
        this.code = code;
        this.expiryTime = expiryTime;
    }

    // 생성자 (expirationMinutes 파라미터 사용)
    public VerificationCode(String email, String code, int expirationMinutes) {
        this.email = email;
        this.code = code;
        this.expiryTime = LocalDateTime.now().plusMinutes(expirationMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}
