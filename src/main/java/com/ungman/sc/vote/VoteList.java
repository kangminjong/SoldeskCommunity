package com.ungman.sc.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "VOTELIST")
public class VoteList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "votelist_seq")
    @SequenceGenerator(sequenceName = "VOTELIST_SEQ", name = "votelist_seq", allocationSize = 1)
    @Column(name = "VOTELIST_ID")
    private Integer voteListId; // 선택지 ID

    @ManyToOne
    @JoinColumn(name = "VOTE_ID", nullable = false)
    private Vote vote; // Vote 객체로 참조 (voteId가 아닌 vote 필드를 사용)

    @Column(name = "CHOICES", nullable = false)
    private String choice; // 선택지 제목

    @Column(nullable = false)
    private Integer voteCount = 0;  // 기본값을 0으로 설정

}
