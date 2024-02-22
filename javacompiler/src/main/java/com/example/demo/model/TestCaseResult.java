package com.example.demo.model;

public class TestCaseResult {
	private String input;
    private String expectedOutput;
    private String actualOutput;
    private boolean testCasePassed;
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getExpectedOutput() {
		return expectedOutput;
	}
	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
	public String getActualOutput() {
		return actualOutput;
	}
	public void setActualOutput(String actualOutput) {
		this.actualOutput = actualOutput;
	}
	public boolean isTestCasePassed() {
		return testCasePassed;
	}
	public void setTestCasePassed(boolean testCasePassed) {
		this.testCasePassed = testCasePassed;
	}

}
