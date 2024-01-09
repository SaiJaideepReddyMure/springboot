package com.example.demo.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Question;
import com.example.demo.model.UserAnswers;
//import com.example.demo.service.QuestionService;
import com.example.demo.service.QuizService;

@RestController

public class quizController {
	@Autowired 
	//private QuestionService questionService;
	private  final QuizService quizService;
	public  quizController(QuizService quizService) {
        this.quizService = quizService;
    }
	 

	@GetMapping("/quiz")
    public ModelAndView startQuiz() {
        List<Question> questions = quizService.getQuizQuestions();
        Collections.shuffle(questions);

        questions.forEach(question -> {
            List<String> shuffledOptions = question.shuffleOptions();
            question.setOptions(shuffledOptions);
        });

        ModelAndView mv = new ModelAndView("quiz");
        mv.addObject("questions", questions);
        mv.addObject("userAnswers", new UserAnswers());

        return mv;
    }
	@PostMapping("/submitQuiz")
    public ModelAndView submitQuiz(@ModelAttribute("userAnswers") UserAnswers userAnswers) {
		List<Question> questions = quizService.getQuizQuestions();
        int score = quizService.calculateScore(questions, userAnswers.getUserAnswers());

        ModelAndView mv = new ModelAndView("resultPage");
        mv.addObject("correctAnswers", score);return mv;
    }
}
