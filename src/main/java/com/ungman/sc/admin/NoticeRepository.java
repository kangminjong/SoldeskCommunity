package com.ungman.sc.admin;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ungman.sc.board.Board;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Integer> {
	public abstract List<Notice> findAll(Pageable pg);

	public abstract Long countByNoticeTitleContainingOrNoticeContentContaining(String title, String content);
	
	public abstract List<Notice> findByNoticeTitleContainingOrNoticeContentContaining(Pageable pg, String title, String content);
	
	public abstract void flush();

	public abstract Notice findByNoticeID(Integer noticeID);
	
	public abstract List<Notice> findAll();
}
