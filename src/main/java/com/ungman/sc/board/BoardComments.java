package com.ungman.sc.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

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
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "COMMENTS")
public class BoardComments {
	@Id
	@SequenceGenerator(sequenceName = "COMMENT_SEQ", name = "cs", allocationSize = 1)  
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cs")
	@Column(name = "COMMENT_ID")
	private Integer commentId;

	@ManyToOne
	@JoinColumn(name = "BOARD_ID")
	private Board board;

	@Column(name = "CONTENT")
	private String content;

	@CreationTimestamp
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "USER_EMAIL")
	private User userEmail;

	@Column(name = "HIDE_NAME")
	private String hideName;

	@OneToMany(mappedBy = "commentN")
	private List<BoardCOComments> coCommentss2;
}