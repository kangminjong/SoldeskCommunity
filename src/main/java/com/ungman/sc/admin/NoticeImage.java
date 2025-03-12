package com.ungman.sc.admin;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notice_image")
public class NoticeImage {

	@Id
	@SequenceGenerator(sequenceName = "notice_image_SEQ", name = "nis", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nis")
	@Column(name = "notice_image_ID")
	private Integer noticeImageId;
	
	@Column(name = "notice_image_url")
	private String noticeImageUrl;
	
	@ManyToOne
	@JoinColumn(name = "notice_ID")
	private Notice noticeID;
}
