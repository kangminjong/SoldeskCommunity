package com.ungman.sc.admin;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notice")
public class Notice {
	
	@Id
	@SequenceGenerator(sequenceName = "notice_SEQ", name = "nos", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nos")
	@Column(name = "notice_ID")
	private Integer noticeID;
	
	@Column(name = "notice_title")
	private String noticeTitle;
	
	@Column(name = "notice_content")
	private String noticeContent;
	
	@UpdateTimestamp
	@Column(name = "notice_date")
	private Date noticeDate;
	
}
