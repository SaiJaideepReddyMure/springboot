package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
 

import com.example.demo.model.QuestionForm;
import com.example.demo.model.Questions;
import com.example.demo.model.exam_data;
 
import com.example.demo.service.QuestionService;
import com.example.demo.service.examService;


@Controller
public class QuestionController {
 
    @Autowired
    private  final QuestionService questionsService;
    private  final examService examservice;
    
    public QuestionController(QuestionService questionsService, examService examservice) {
		 
		this.questionsService = questionsService;
		this.examservice = examservice;
	}

	@GetMapping("/uploadQues")
    public String uploadques(Model model)
    {
		 model.addAttribute("QuestionForm",new QuestionForm());
    	return "uplq";
    }
 
	@PostMapping("/addQuestions")
    public String addQuestions(@ModelAttribute QuestionForm questionForm) {
        
		System.out.println("QuestionForm: " + questionForm);
	    System.out.println("Quiz Time: " + questionForm.getQuizTime() + " minutes");
	    System.out.println("Quiz Date and Time: " + questionForm.getQuizDateTime());
	    int quizTime = questionForm.getQuizTime(); 
        LocalDateTime quizDateTime = questionForm.getQuizDateTime();  
	    exam_data ed = new exam_data();
        ed.setClass_id("del_id");
        ed.setNo_questions(questionForm.getQuestionList().size());
        ed.setTimer(quizTime);
        ed.setTimeact(quizDateTime);
        examservice.Saveentity(ed);
        
        
        // inserting into the Question database
        
        int x = ed.getExam_id();
        List<Questions> questions = questionForm.getQuestionList();
        for(Questions question : questions)
        {
        	question.setExam_id(x);
        }
        questionsService.saveAll(questions);

        
         

        return "redirect:/";
    }

}
