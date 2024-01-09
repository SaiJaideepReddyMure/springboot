package com.example.demo.model;

import java.util.Map;

public class UserAnswers{
	
	private Map<Long , String> userAnswers;

	public Map<Long, String> getUserAnswers() {
		return userAnswers;
	}

	public void setUserAnswers(Map<Long, String> userAnswers) {
		this.userAnswers = userAnswers;
	}

}
