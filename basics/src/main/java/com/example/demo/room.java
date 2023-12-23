package com.example.demo;

import org.springframework.stereotype.Component;

//@Component
@Component("rooms") //to specify a class name differently 
public class room {
	private int roomno;
	private int subject;
	public int getRoomno() {
		return roomno;
	}
	public void setRoomno(int roomno) {
		this.roomno = roomno;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public void check()
	{
		System.out.println("Class is there");
	}

}
