package com.example.demo;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Configurable

public class BasicsApplication {
	public static void main(String[] args) {
		 ConfigurableApplicationContext context = SpringApplication.run(BasicsApplication.class, args);
		 System.out.println("hi");
		 Student s = context.getBean(Student.class);
		 s.reg();
		 // need to create two then 
		 Student s1 = context.getBean(Student.class);
		 s1.reg();
	}

}
