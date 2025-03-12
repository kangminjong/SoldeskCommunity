package com.ungman.sc.admin;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeImageRepository extends CrudRepository<NoticeImage, Integer>{
	public abstract NoticeImage findByNoticeID(Notice n);

	public abstract void flush();
}
