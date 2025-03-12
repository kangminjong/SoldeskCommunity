package com.ungman.sc.board;

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
@Entity(name = "BOARD_FILE")
public class BoardFile {
	  
	@Id
	@SequenceGenerator(sequenceName = "BOARD_File_SEQ", name = "bofs", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bofs")
	@Column(name = "BOARD_FILE_ID")
	private Integer boardFileId;
	
	@Column(name = "BOARD_FILE_URL")  
	private String boardFileUrl;
	
	@ManyToOne
	@JoinColumn(name = "BOARD_ID")
	private Board boardID;
	
}

