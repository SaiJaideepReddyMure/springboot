package com.example.demo.model;

public class Code {
    private String sourceCode;
    private int questionId;

    // other properties and methods...

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    public Code() {
    }
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}