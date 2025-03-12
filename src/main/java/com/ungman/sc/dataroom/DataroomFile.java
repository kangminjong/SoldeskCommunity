package com.ungman.sc.dataroom;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SequenceGenerator(name = "dataroom_seq", sequenceName = "DATAROOM_SEQ", allocationSize = 1)
@Table(name = "DATAROOM")

public class DataroomFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dataroom_seq")  
	@Column(name = "DR_ID")
	private Integer no;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "DR_FILE")
	private String file;

	@CreationTimestamp
	@Column(name = "CREATED_DATE")
	private Date date;
	
	@Column(name = "USER_EMAIL")
	private String userEmail;
	
	@Column(name = "CLASS_ID")
	private Integer classId;
}
