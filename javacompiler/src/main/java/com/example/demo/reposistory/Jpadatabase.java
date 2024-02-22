package com.example.demo.reposistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Questions;

// Question repo 
public interface Jpadatabase extends JpaRepository<Questions,Integer>{
	@Query(value="SELECT * FROM questions q WHERE q.exam_id = ?1", nativeQuery = true)
    List<Questions> data(int examId);
	


}
