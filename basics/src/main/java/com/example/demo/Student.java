package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype") // for creating a multiple objects

public class Student {
	private int id;
	private String name;
	private String course;
	@Autowired
	@Qualifier("rooms") // to search for any class there of name given
	public room room1;
	
	public Student() {
		super();
		System.out.println("Student created");
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public void reg() {
		System.out.println("Registered sucessfully");
		room1.check();
	}

}
