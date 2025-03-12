package com.ungman.sc.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteUserRepository extends JpaRepository<VoteUser, Integer> {

    // 특정 투표에서 사용자가 이미 투표했는지 확인
    List<VoteUser> findByVoteAndUserEmail(Vote vote, String userEmail);

    // 특정 사용자가 투표한 모든 선택지 조회
    List<VoteUser> findByUserEmail(String userEmail);
    
    // 기존 투표 내역 삭제 (Vote 객체 참조 대신 voteId 사용)
    void deleteByUserEmailAndVote_VoteId(String userEmail, Integer voteId);

    // 특정 투표와 선택지에 대해 사용자가 이미 투표한 내역 확인
    List<VoteUser> findByVoteAndVoteListAndUserEmail(Vote vote, VoteList voteList, String userEmail);
    
    Optional<VoteUser> findByVoteUserId(Integer voteUserId);
}

