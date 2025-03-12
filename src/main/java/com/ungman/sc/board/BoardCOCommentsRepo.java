package com.ungman.sc.board;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCOCommentsRepo extends CrudRepository<BoardCOComments, Integer> {  

}
