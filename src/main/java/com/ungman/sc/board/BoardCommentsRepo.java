package com.ungman.sc.board;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCommentsRepo extends CrudRepository<BoardComments, Integer> {
	
	public abstract Optional<BoardComments> findBycommentId(Integer commentsId);  
}
