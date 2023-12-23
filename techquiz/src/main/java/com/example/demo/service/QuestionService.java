package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.model.Question;
import com.example.demo.repo.QuestionRepository;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public ResponseEntity<List<Question>> getAllQuestions() {
    	try {
    		
    	
        return new ResponseEntity<>(questionRepository.findAll(),HttpStatus.OK);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

	public ResponseEntity<List<Question>> getQuestionsByTech(String tech) {
		// TODO Auto-generated method stub
		try {
    		
	    	
	        return new ResponseEntity<>(questionRepository.findBytech(tech),HttpStatus.OK);
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
	}

	public String addQuestion(Question question) {
		 questionRepository.save(question);
		 return "Sucess";
		
	}

	 
}
