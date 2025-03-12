package com.ungman.sc.board;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.ungman.sc.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "CO_COMMENTS")
public class BoardCOComments {
	@Id
	@SequenceGenerator(sequenceName = "CO_COMMENT_SEQ", name = "ccs", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ccs")
	@Column(name = "CO_COMMENT_ID")
	private Integer cocommentId;  

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

	@ManyToOne
	@JoinColumn(name = "COMMENT_ID", nullable = true)
	private BoardComments commentN;

  
	
}
