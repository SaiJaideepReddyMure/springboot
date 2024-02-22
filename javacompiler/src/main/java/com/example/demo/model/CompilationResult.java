package com.example.demo.model;

import java.util.List;

public class CompilationResult {
    private String output;
    private List<TestCaseResult> outputResults;
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public List<TestCaseResult> getOutputResults() {
		return outputResults;
	}
	public void setOutputResults(List<TestCaseResult> outputResults) {
		this.outputResults = outputResults;
	}

     
}
