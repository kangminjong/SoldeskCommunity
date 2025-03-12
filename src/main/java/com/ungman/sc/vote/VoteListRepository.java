package com.ungman.sc.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteListRepository extends JpaRepository<VoteList, Integer> {

    // 특정 투표에 속한 모든 선택지 목록 조회
    List<VoteList> findByVote(Vote vote); // Vote 객체로 조회

    // 선택지 항목 ID로 조회
    Optional<VoteList> findByVoteListId(Integer voteListId);
}

