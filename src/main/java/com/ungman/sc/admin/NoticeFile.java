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
@Entity(name = "notice_file")
public class NoticeFile {
	@Id
	@SequenceGenerator(sequenceName = "notice_file_SEQ", name = "nfs", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nfs")
	@Column(name = "notice_file_ID")
	private Integer noticeFileId;
	
	@Column(name = "notice_file_url")
	private String noticeFileUrl;
	
	@ManyToOne
	@JoinColumn(name = "notice_ID")
	private Notice noticeID;
}
