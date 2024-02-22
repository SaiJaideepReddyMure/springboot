package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionForm {
	private List<Questions> questionList;
    private int quizTime;
    private LocalDateTime quizDateTime;
    public QuestionForm() {
    }
    public List<Questions> getQuestionList() {
		return questionList;
	}
	public void setQuestionList(List<Questions> questionList) {
		this.questionList = questionList;
	}
	public int getQuizTime() {
		return quizTime;
	}
	public void setQuizTime(int quizTime) {
		this.quizTime = quizTime;
	}
	public LocalDateTime getQuizDateTime() {
		return quizDateTime;
	}
	public void setQuizDateTime(LocalDateTime quizDateTime) {
		this.quizDateTime = quizDateTime;
	}

}
