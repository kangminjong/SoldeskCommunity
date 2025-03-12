package com.ungman.sc.board;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Integer> {
	public abstract List<Board> findAll(Pageable pg);

	public abstract Long countByTitleContainingOrContentContainingOrHideNameAndUserEmailNameContaining(
		    String title, String content, String hideName, String name);

	public abstract List<Board> findByTitleContainingOrContentContainingOrHideNameAndUserEmailNameContaining(
		    Pageable pg, String title, String content, String hideName, String name);

	public abstract void flush();

	public abstract List<Board> findByUserEmailIsDeletedNot(String string);

	// public abstract Board findByNo(Integer no);

	public List<Board> findAll();

	public Optional<Board> findById(Integer boardId);

	public void delete(Board board);
}
