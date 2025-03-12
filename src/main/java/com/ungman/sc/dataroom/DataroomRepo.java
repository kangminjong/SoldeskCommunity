package com.ungman.sc.dataroom;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataroomRepo extends CrudRepository<DataroomFile, Integer> {
	public abstract List<DataroomFile> findByCategoryOrderByDateDesc(String c);  
}
