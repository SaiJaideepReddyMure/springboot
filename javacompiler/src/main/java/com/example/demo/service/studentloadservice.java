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
	

}
