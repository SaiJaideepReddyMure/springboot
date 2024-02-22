package com.example.demo.model;
 import java.time.LocalDateTime;

 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class exam_data {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  int exam_id;
	private String class_id;
	private int no_questions;
	private LocalDateTime timeact;
	private int timer;
	 
	 
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id( int exam_id) {
		this.exam_id = exam_id;
	}
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public int getNo_questions() {
		return no_questions;
	}
	public void setNo_questions(int no_questions) {
		this.no_questions = no_questions;
	}
	public LocalDateTime getTimeact() {
		return timeact;
	}
	public void setTimeact(LocalDateTime timeact) {
		this.timeact = timeact;
	}
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}

}
