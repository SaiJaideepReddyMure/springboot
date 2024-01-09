package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.questionRepository;
import com.example.demo.model.Question;
@Service
public class QuestionService {
	 @Autowired
	    private questionRepository QuestionRepository;

	public List<Question> getAllQuestions() {
		// TODO Auto-generated method stub
		return QuestionRepository.findAll() ;
	}

}
