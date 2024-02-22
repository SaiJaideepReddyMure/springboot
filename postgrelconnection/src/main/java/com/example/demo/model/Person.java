package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
 
@Entity

public class Person {
	@Id
	private long id;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
