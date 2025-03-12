package com.ungman.sc.board;


import java.util.Date; 
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.ungman.sc.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;  
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "BOARD")
public class Board {

	@Id
	@SequenceGenerator(sequenceName = "BOARD_SEQ", name = "bos", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bos")
	@Column(name = "BOARD_ID")
	private Integer boardID;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "COMMENT_COUNT")
	private Integer commentCount;

	@UpdateTimestamp
	@Column(name = "BOARD_DATE")
	private Date boardDate;

	@Column(name = "HIDE_NAME")
	private String hideName;

	@ManyToOne
	@JoinColumn(name = "USER_EMAIL")
	private User userEmail;
	
	@OneToMany(mappedBy = "board")
	private List<BoardComments> commentss;
}
