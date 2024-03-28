package com.example.demo.service;

 

import com.example.demo.model.CompilationResult;

public interface CompilerService {
	 

    CompilationResult compileAndRunWithTestCases(String sourceCode, String[] openTestCases, String[] openExpectedOutputs);

	CompilationResult compileAndRunWithTestCasesForPython(String sourceCode, String[] openTestCases,
			String[] openExpectedOutputs);

	CompilationResult compileAndRunWithTestCasesForC(String sourceCode, String[] openTestCases,
			String[] openExpectedOutputs);
	
    }
