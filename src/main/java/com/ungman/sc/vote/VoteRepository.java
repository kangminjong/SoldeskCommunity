package com.ungman.sc.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    // 특정 투표 ID로 조회
    Optional<Vote> findById(Integer voteId);

    // 모든 투표 목록 조회
    List<Vote> findByClassId(Integer classId);
}
