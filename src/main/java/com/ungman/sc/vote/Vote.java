package com.ungman.sc.vote;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "VOTE")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vote_seq")
    @SequenceGenerator(sequenceName = "VOTE_SEQ", name = "vote_seq", allocationSize = 1)
    @Column(name = "VOTE_ID")
    private Integer voteId; // 투표 ID

    @Column(name = "CLASS_ID", nullable = false)
    private Integer classId;

    @Column(name = "USER_EMAIL", nullable = false)
    private String userEmail;

    @NotNull
    @Size(min = 1, message = "제목은 필수 입력 항목입니다.")
    @Column(name = "TITLE", nullable = false)
    private String title;

    @NotNull
    @FutureOrPresent(message = "시작 날짜는 현재 날짜 이후여야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @NotNull
    @FutureOrPresent(message = "끝 날짜는 현재 날짜 이후여야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "END_DATE", nullable = false)
    private Date endDate;

    @Column(name = "MULTICHOICE", nullable = false)
    private String multiChoice = "N";

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true) // mappedBy를 "vote"로 수정
    private List<VoteList> voteLists; // 관련된 선택지들
    
    
    
}
