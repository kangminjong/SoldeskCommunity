package com.ungman.sc.board;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


@Service
public interface BoardImageRepository extends CrudRepository<BoardImage, Integer>{
	public abstract BoardImage  findByboardID(Board board);
	public abstract BoardImage findByboardImageID(Integer boardImageID);  

}   
