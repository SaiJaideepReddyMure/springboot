package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.CompilationResult;
import com.example.demo.model.TestCaseResult;

public class CompilerServiceImpl implements CompilerService {

    @Override
    public CompilationResult compileAndRunWithTestCases(String sourceCode, String[] inputList, String[] expectedOutputList) {
        CompilationResult compilationResult = new CompilationResult();
        List<TestCaseResult> testCaseResults = new ArrayList<>();
        StringBuilder result = new StringBuilder();

        try {
            String className = "Main";

            // Create a temporary directory for storing the compiled class
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File javaFile = new File(tempDir, className + ".java");

            // Write the source code to the temporary file
            CompilerUtils.writeToFile(javaFile, sourceCode);

            // Compile the Java file
            Process compilationProcess = new ProcessBuilder("javac", javaFile.getPath())
                    .redirectErrorStream(false) // Ensure error stream is not merged with output
                    .start();

            // Capture standard output and error separately
            ByteArrayOutputStream compilationOutput = new ByteArrayOutputStream();
            ByteArrayOutputStream compilationErrorOutput = new ByteArrayOutputStream();
            CompilerUtils.copyStream(compilationProcess.getInputStream(), compilationOutput);
            CompilerUtils.copyStream(compilationProcess.getErrorStream(), compilationErrorOutput);

            // Wait for the compilation process to complete
            int compilationExitCode = compilationProcess.waitFor();

            if (compilationExitCode != 0) {
                // Compilation error occurred
                String compilationErrorMessage = compilationErrorOutput.toString(StandardCharsets.UTF_8).trim();
                result.append("Compilation Error:\n")
                        .append(compilationErrorMessage)
                        .append("\n");

                // Set the compilation error message as the actual output for all test cases
                for (int i = 0; i < inputList.length; i++) {
                    TestCaseResult testCaseResult = new TestCaseResult();
                    testCaseResult.setInput(inputList[i]);
                    testCaseResult.setExpectedOutput(expectedOutputList[i]);
                    testCaseResult.setActualOutput(compilationErrorMessage); // Set compilation error message
                    testCaseResult.setTestCasePassed(false); // Set test case as failed
                    testCaseResults.add(testCaseResult);
                }

                // Set the error message in CompilationResult
                compilationResult.setOutput(result.toString());
            } else {
                // Compilation successful, run the test cases
                for (int i = 0; i < inputList.length; i++) {
                    String input = inputList[i];

                    // Execute the Java program
                    Process executionProcess = new ProcessBuilder("java", "-cp", tempDir.getPath(), className)
                            .redirectErrorStream(false) // Ensure error stream is not merged with output
                            .start();

                    // Provide input to the process
                    CompilerUtils.writeToProcessInputStream(executionProcess, input);

                    // Capture the output of the process
                    ByteArrayOutputStream executionOutput = new ByteArrayOutputStream();
                    CompilerUtils.copyStream(executionProcess.getInputStream(), executionOutput);

                    // Wait for the execution process to complete
                    executionProcess.waitFor();

                    // Get the actual output
                    String actualOutput = executionOutput.toString(StandardCharsets.UTF_8).trim();

                    // Compare actual output with expected output
                    boolean testCasePassed = actualOutput.equals(expectedOutputList[i]);

                    // Create a TestCaseResult object and add it to the list
                    TestCaseResult testCaseResult = new TestCaseResult();
                    testCaseResult.setInput(input);
                    testCaseResult.setExpectedOutput(expectedOutputList[i]);
                    testCaseResult.setActualOutput(actualOutput);
                    testCaseResult.setTestCasePassed(testCasePassed);
                    testCaseResults.add(testCaseResult);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            result.append("Error: ").append(e.getMessage());

            // Store error message in the output result
            compilationResult.setOutput(result.toString());
        }

        // Store test case results in the output result
        compilationResult.setOutputResults(testCaseResults);
        return compilationResult;
    }
}
