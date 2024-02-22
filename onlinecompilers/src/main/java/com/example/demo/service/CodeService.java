package com.example.demo.service;

import java.io.*;
import java.nio.file.*;
import org.springframework.stereotype.Service;

@Service
public class CodeService {

    public String compileAndRunCode(String code) {
        try {
            File sourceFile = writeCodeToFile(code);

            Object compilationResult = compileCode(sourceFile);

            if (compilationResult instanceof String) {
                // Compilation failed, return the error messages
                return (String) compilationResult;
            } else {
                // Compilation succeeded, run the code
                return runCompiledCode(sourceFile);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private File writeCodeToFile(String code) throws IOException {
        Path sourceFilePath = Files.createTempFile("Main", ".java");
        try (BufferedWriter writer = Files.newBufferedWriter(sourceFilePath, StandardOpenOption.WRITE)) {
            writer.write(code);
        }

        // Rename the file to Main.java
        Files.move(sourceFilePath, sourceFilePath.resolveSibling("Main.java"), StandardCopyOption.REPLACE_EXISTING);

        // Return the File object for the renamed file
        return sourceFilePath.resolveSibling("Main.java").toFile();
    }

    private Object compileCode(File sourceFile) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("javac", sourceFile.getPath());
        processBuilder.redirectErrorStream(true); // Redirect error stream to input stream
        Process compilationProcess = processBuilder.start();

        // Read the error stream and print/log the messages
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(compilationProcess.getInputStream()))) {
            StringBuilder compilationOutput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                compilationOutput.append(line).append("\n");
            }

            int compilationResult;
            try {
                compilationResult = compilationProcess.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                compilationResult = -1; // Indicates an interruption during compilation
            }

            // Return compilation output as a string if there was an error
            if (compilationResult != 0) {
                return compilationOutput.toString();
            } else {
                // Return the compilation result as an integer
                return compilationResult;
            }
        }
    }

    private String runCompiledCode(File sourceFile) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", sourceFile.getParentFile().getPath(), "Main");
        processBuilder.directory(sourceFile.getParentFile());

        Process executionProcess = processBuilder.start();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        Thread outputThread = redirectStream(executionProcess.getInputStream(), outputStream);
        Thread errorThread = redirectStream(executionProcess.getErrorStream(), errorStream);

        executionProcess.waitFor();
        outputThread.join();
        errorThread.join();

        return    outputStream.toString() + "" + errorStream.toString();
    }

    private Thread redirectStream(InputStream input, OutputStream output) {
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                 PrintWriter writer = new PrintWriter(output, true)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        return thread;
    }
    public static class TestCase {
        private final String input;
        private final String expectedOutput;

        public TestCase(String input, String expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        public String getInput() {
            return input;
        }

        public String getExpectedOutput() {
            return expectedOutput;
        }
    }

    // Inner class to represent a test result
    public static class TestResult {
        private final String input;
        private final String actualOutput;
        private final String expectedOutput;

        public TestResult(String input, String actualOutput, String expectedOutput) {
            this.input = input;
            this.actualOutput = actualOutput;
            this.expectedOutput = expectedOutput;
        }

        public String getInput() {
            return input;
        }

        public String getActualOutput() {
            return actualOutput;
        }

        public String getExpectedOutput() {
            return expectedOutput;
        }
    }
    
}
