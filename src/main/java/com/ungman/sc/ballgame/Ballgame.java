package com.ungman.sc.ballgame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Ballgame")
public class Ballgame {

	@Id
	@SequenceGenerator(name = "ballgame_seq", sequenceName = "ballgame_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ballgame_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "player_name")
	private String playerName;

	@Column(name = "score")
	private int score;

	@Column(name = "timestamp")
	private Date date = new Date();
}
