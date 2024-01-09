package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Question;
@Service
public class QuizService {
	@Autowired
    private QuestionService questionService;

    public List<Question> getQuizQuestions() {
        List<Question> questions = questionService.getAllQuestions();

         
         

        return questions;
    }

    public int calculateScore(List<Question> questions, Map<Long, String> userAnswers) {
        int score = 0;
        for (Question question : questions) {
            String userAnswer = userAnswers.get(question.getId());
            System.out.println("Question: " + question.getText());
            System.out.println("User Answer: " + userAnswer);
            System.out.println("Correct Answer: " + question.getCorrectanswer());
            if (userAnswer != null && userAnswer.equalsIgnoreCase(question.getCorrectanswer())) {
                score++;
            }
        }
        return score;
    }
     

}
