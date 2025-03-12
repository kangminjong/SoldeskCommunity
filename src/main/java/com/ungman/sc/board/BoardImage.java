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
@Entity(name = "BOARD_IMAGE")
public class BoardImage {

	@Id
	@SequenceGenerator(sequenceName = "BOARD_IMAGE_SEQ", name = "bois", allocationSize = 1)   
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bois")
	@Column(name = "BOARD_IMAGE_ID")
	private Integer boardImageID;
	
	@Column(name = "BOARD_IMAGE_URL")
	private String boardImageUrl;
	
	@ManyToOne
	@JoinColumn(name = "BOARD_ID")
	private Board boardID;
	
	
	
	
}
