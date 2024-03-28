package com.example.demo.reposistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.studentload;

import jakarta.transaction.Transactional;

@Repository
public interface studentloaddata extends JpaRepository<studentload,Long>{
	
    @Query(value="select exam_id from studentload where reg_no=?1 ",nativeQuery=true)
	public List<Integer> setsexam_id(String reg_no);
    
    @Query(value="select  submit from studentload where exam_id=?1 AND reg_no=?2 ",nativeQuery=true)
	public boolean checkstatus(Integer exam_id, String reg_no);
    @Transactional
    @Modifying
    @Query(value = "UPDATE studentload SET submit = true WHERE exam_id = ?1 AND reg_no = ?2", nativeQuery = true)
    public Object setstatus(int exam_id, String reg_no);
    @Transactional
    @Modifying
    @Query(value = "UPDATE studentload SET timer = ?2 WHERE exam_id = ?3 AND reg_no = ?1",nativeQuery = true)
	public void savingtime(String reg_no, int hour, int examId);
     
    @Query(value="select timer from studentload where exam_id=?2 AND reg_no=?1",nativeQuery=true)
	public int get_timer(String reg_no, Integer exam_id);
}
