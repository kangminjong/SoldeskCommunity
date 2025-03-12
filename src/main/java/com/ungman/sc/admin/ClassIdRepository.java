package com.ungman.sc.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassIdRepository extends CrudRepository<ClassId, Integer>{
   public abstract List<ClassId> findAllByOrderByClassIdAsc();
   
   public abstract Optional<ClassId> findByClassId(Integer classId);
   
   
}
