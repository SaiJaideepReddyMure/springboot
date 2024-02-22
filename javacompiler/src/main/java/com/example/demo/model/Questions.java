package com.example.demo.model;

  
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
 
 

@Entity

public class Questions {
	
    @Id
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int Exam_id;
   

     
	
	 
	public int getExam_id() {
		return Exam_id;
	}
	public void setExam_id(int exam_id) {
		Exam_id = exam_id;
	}
	private String Ques;

    
	 
	private String[] opentestCases;

	 private String[] opentestCasesoutput;

	 private String[] closedtestCases;

	 private String[] closedtestCasesoutput;

     
	 
	public String[] getOpentestCases() {
		return opentestCases;
	}
	public void setOpentestCases(String[] opentestCases) {
		this.opentestCases = opentestCases;
	}
	public String[] getOpentestCasesoutput() {
		return opentestCasesoutput;
	}
	public void setOpentestCasesoutput(String[] opentestCasesoutput) {
		this.opentestCasesoutput = opentestCasesoutput;
	}
	public String[] getClosedtestCases() {
		return closedtestCases;
	}
	public void setClosedtestCases(String[] closedtestCases) {
		this.closedtestCases = closedtestCases;
	}
	public String[] getClosedtestCasesoutput() {
		return closedtestCasesoutput;
	}
	public void setClosedtestCasesoutput(String[] closedtestCasesoutput) {
		this.closedtestCasesoutput = closedtestCasesoutput;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQues() {
		return Ques;
	}
	public void setQues(String ques) {
		Ques = ques;
	}
	 
	 

}
