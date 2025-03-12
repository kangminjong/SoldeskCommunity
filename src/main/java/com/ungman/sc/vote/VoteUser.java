package com.ungman.sc.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "VOTE_USER")
public class VoteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vote_user_seq")
    @SequenceGenerator(sequenceName = "VOTE_USER_SEQ", name = "vote_user_seq", allocationSize = 1)
    @Column(name = "VOTE_USER_ID")
    private Integer voteUserId; // 투표 기록 ID

    @ManyToOne
    @JoinColumn(name = "VOTE_ID", nullable = false)
    private Vote vote; // Vote 객체로 참조 (voteId가 아닌 vote 필드를 사용)

    @ManyToOne
    @JoinColumn(name = "VOTELIST_ID", nullable = false)
    private VoteList voteList; // VoteList 객체로 참조 (voteListId가 아닌 voteList 필드를 사용)

    @Column(name = "USER_EMAIL", nullable = false)
    private String userEmail; // 투표한 사용자
}
