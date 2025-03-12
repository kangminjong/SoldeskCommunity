package com.ungman.sc.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public abstract List<User> findAll();
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u.classId FROM User u WHERE u.email = :email")
    Integer findClassIdByEmail(@Param("email") String email);

    // 이름과 전화번호로 이메일 찾기
    @Query("SELECT u.email FROM User u WHERE u.name = :name AND u.tel = :tel")
    Optional<String> findEmailByNameAndTel(@Param("name") String name, @Param("tel") String tel);  
}