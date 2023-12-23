package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Question;
import com.example.demo.service.QuestionService;

@RestController
@RequestMapping("/question")
public class QuizController {
	
	@Autowired
	
	private QuestionService questionService;
	@GetMapping("allQuestions")
	public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }
 
	@GetMapping("tech/{tech}")
	public ResponseEntity<List<Question>> getQuestionByTech(@PathVariable("tech") String  tech)
	{
		return questionService.getQuestionsByTech(tech);
	}
	@PostMapping("add")
	public String addQuestion(@RequestBody Question question)
	{
		 return questionService.addQuestion(question);
	}
	 
	

}
