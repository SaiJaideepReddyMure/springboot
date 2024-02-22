package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.model.exam_data;
import com.example.demo.reposistory.examdata_repo;

@Service
public class examService {
	private final  examdata_repo erepo;
	public examService(examdata_repo erepo) {
	 
		this.erepo = erepo;
	}
	public void Saveentity(exam_data examdata)
	{
		erepo.save(examdata);
	}
	
 
	public String gettingclass_id(int exam_id)
	{
		return erepo.findClassIdsForExam(exam_id);
	}
	public LocalDateTime getTimmeract(int exam_ids) {
		// TODO Auto-generated method stub
		return  erepo.findtimeactBy(exam_ids);
	}
	public int getTimmer(Integer exam_id) {
		// TODO Auto-generated method stub
		return  erepo.gettimer(exam_id);
	}

}
