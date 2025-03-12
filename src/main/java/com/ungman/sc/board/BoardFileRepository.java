 package com.ungman.sc.board;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface BoardFileRepository extends CrudRepository<BoardFile, Integer>{
	public abstract BoardFile findByboardID(Board board);

	public abstract BoardFile findByboardFileId(Integer boardFileId);  
}
   