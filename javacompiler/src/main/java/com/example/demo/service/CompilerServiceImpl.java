package com.example.demo.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    public CompilationResult compileAndRunWithTestCasesForPython(String sourceCode, String[] inputList, String[] expectedOutputList) {
        CompilationResult compilationResult = new CompilationResult();
        List<TestCaseResult> testCaseResults = new ArrayList<>();
        StringBuilder result = new StringBuilder();

        try {
            // Write the source code to a temporary file
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File pythonFile = new File(tempDir, "main.py");
            CompilerUtils.writeToFile(pythonFile, sourceCode);

            // Iterate over each input
            for (int i = 0; i < inputList.length; i++) {
                String[] inputs = inputList[i].split(" "); // Split input string into individual values

                // Execute the Python script with input values
                ProcessBuilder processBuilder = new ProcessBuilder("python", pythonFile.getPath())
                        .redirectErrorStream(true); // Merge error stream with output stream
                Process executionProcess = processBuilder.start();

                // Provide inputs to the process from the backend
                try (OutputStream outputStream = executionProcess.getOutputStream();
                     InputStream inputStream = executionProcess.getInputStream();
                     InputStream errorStream = executionProcess.getErrorStream()) {

                    // Write input to the process
                    for (String input : inputs) {
                        // Determine the data type of the input value and parse accordingly
                        if (input.matches("-?\\d+")) { // Integer
                            int intValue = Integer.parseInt(input);
                            outputStream.write((intValue + "\n").getBytes());
                        } else if (input.matches("-?\\d*\\.\\d+")) { // Float
                            float floatValue = Float.parseFloat(input);
                            outputStream.write((floatValue + "\n").getBytes());
                        } else if (input.matches("-?\\d+\\.\\d+")) { // Double
                            double doubleValue = Double.parseDouble(input);
                            outputStream.write((doubleValue + "\n").getBytes());
                        } else { // String
                            outputStream.write((input + "\n").getBytes());
                        }
                    }

                    outputStream.flush(); // Ensure data is sent
                    outputStream.close(); // Close the input stream to signal end of input

                    // Read output from the process
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                    StringBuilder outputBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputBuilder.append(line).append("\n");
                    }
                     

                    // Read error output from the process and append it to the output
                    while ((line = errorReader.readLine()) != null) {
                        outputBuilder.append(line).append("\n");
                    }
                    String finalOutput = outputBuilder.toString().trim();

                    // Compare actual output with expected output
                    boolean testCasePassed = finalOutput.equals(expectedOutputList[i]);

                    // Create a TestCaseResult object and add it to the list
                    TestCaseResult testCaseResult = new TestCaseResult();
                    testCaseResult.setInput(inputList[i]);
                    testCaseResult.setExpectedOutput(expectedOutputList[i]);
                    testCaseResult.setActualOutput(finalOutput);
                    testCaseResult.setTestCasePassed(testCasePassed);
                    testCaseResults.add(testCaseResult);

                    // Debugging: Print outputs and whether test cases passed
                    
                } catch (IOException e) {
                    // Log the error and continue with the next test case
                    e.printStackTrace();
                    result.append("Error in Test Case ").append(i + 1).append(": ").append(e.getMessage()).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.append("Error: ").append(e.getMessage());
        }

        compilationResult.setOutput(result.toString());
        compilationResult.setOutputResults(testCaseResults);
        return compilationResult;
    }




    public CompilationResult compileAndRunWithTestCasesForC(String sourceCode, String[] inputList, String[] expectedOutputList) {
        CompilationResult compilationResult = new CompilationResult();
        List<TestCaseResult> testCaseResults = new ArrayList<>();
        StringBuilder result = new StringBuilder();

        try {
            // Write the source code to a temporary file
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File cFile = new File(tempDir, "main.c");
            CompilerUtils.writeToFile(cFile, sourceCode);

            // Compile the C program
            Process compileProcess = new ProcessBuilder("gcc", cFile.getAbsolutePath(), "-o", "main")
                    .redirectErrorStream(true) // Merge error stream with output stream
                    .start();

            // Capture compilation output
            StringBuilder compilationOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    compilationOutput.append(line).append("\n");
                }
            }

            // Wait for compilation process to complete
            int compilationExitCode = compileProcess.waitFor();

            if (compilationExitCode != 0) {
                // Compilation error occurred
                String compilationErrorMessage = compilationOutput.toString().trim();
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

                    // Execute the C program
                    Process executionProcess = new ProcessBuilder("./main")
                            .directory(tempDir)
                            .redirectErrorStream(false) // Ensure error stream is not merged with output
                            .start();

                    // Provide input to the process
                    try (OutputStream outputStream = executionProcess.getOutputStream()) {
                        outputStream.write((input + "\n").getBytes());
                        outputStream.flush(); // Ensure data is sent
                    }

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
            compilationResult.setOutput(result.toString());
        }

        compilationResult.setOutputResults(testCaseResults);
        return compilationResult;
    }

}
