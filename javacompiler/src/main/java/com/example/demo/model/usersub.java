package com.example.demo.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
 
@Entity 
public class usersub {
     
	@Id 
	 
	private  String id;
	private int exam_id;
	
	private int qid;
    private String class_id;
    private String reg_no;
    private int score;
    private String cases; 
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	 
    public int getExam_id() {
		return exam_id;
	}

	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}

	public int getQid() {
		return qid;
	}
	 
	public void setQid(int qid) {
		this.qid = qid;
	}

	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public String getReg_no() {
		return reg_no;
	}
	public void setReg_no(String reg_no) {
		this.reg_no = reg_no;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getCases() {
		return cases;
	}
	public void setCases(String cases) {
		this.cases = cases;
	}
	   
 

     
}
