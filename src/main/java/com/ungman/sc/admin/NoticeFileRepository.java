package com.ungman.sc.admin;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeFileRepository extends CrudRepository<NoticeFile, Integer>{

	public abstract NoticeFile findByNoticeID(Notice n);

	public abstract void flush();
}
