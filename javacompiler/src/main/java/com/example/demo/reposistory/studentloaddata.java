package com.example.demo.reposistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.studentload;

@Repository
public interface studentloaddata extends JpaRepository<studentload,Long>{
	
    @Query(value="select exam_id from studentload where reg_no=?1 ",nativeQuery=true)
	public List<Integer> setsexam_id(String reg_no);
    
    @Query(value="select  submit from studentload where exam_id=?1 AND reg_no=?2 ",nativeQuery=true)
	public boolean checkstatus(Integer exam_id, String reg_no);
}
