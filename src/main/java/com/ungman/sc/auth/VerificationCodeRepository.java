package com.ungman.sc.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    // 유효한 인증 코드 찾기
    @Query("SELECT v FROM VerificationCode v WHERE v.email = :email AND v.code = :code AND v.expiryTime > :now")
    Optional<VerificationCode> findValidCode(@Param("email") String email, @Param("code") String code, @Param("now") LocalDateTime now);
    
    // 특정 이메일의 인증 코드 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationCode v WHERE v.email = :email")
    void deleteByEmail(@Param("email") String email);
}

