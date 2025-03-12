package com.ungman.sc.board;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


@Service
public interface WriteBoardRepository extends CrudRepository<Board, Integer>{
	public abstract Optional<Board> findByboardID(Integer id);  
}
    