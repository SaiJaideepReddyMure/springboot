package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.reposistory.studentloaddata;

@Service
public class studentloadservice {
	@Autowired
	private studentloaddata sd;
	public studentloadservice(studentloaddata sd) {
		 
		this.sd = sd;
	}
	 
	 
	public List<Integer> setingExamIds(String reg_no) {
		 
		 
		return sd.setsexam_id(reg_no) ;
	}


	public boolean checkingstatus(Integer exam_id, String reg_no) {
		 
		return  sd.checkstatus(exam_id,reg_no);
	}


	public Object setstatus(int exam_id, String reg_no) {
		 return sd.setstatus(exam_id,reg_no);
		
	}


	public void updatetimer(String reg_no, String timer, String exam_id) {
		int hour = Integer.parseInt(timer.split(":")[0]);
		int ExamId = Integer.parseInt(exam_id);
		sd.savingtime(reg_no,hour,ExamId);
		
	}


	public int loading_timer(String reg_no, Integer exam_id) {
		// TODO Auto-generated method stub
		return sd.get_timer(reg_no,exam_id);
	}


	 


	 
	

}
