package com.example.demo.service;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class CompilerService {
    public CompilationResult compileAndRun(String code, String language) {
        switch (language.toLowerCase()) {
            case "java":
                return compileAndRunJava(code);
            case "python":
                return compileAndRunPython(code);
            case "c":
                return compileAndRunC(code);
            default:
                return new CompilationResult(false, "Unsupported language: " + language);
        }
    }
    public Map<String, Object> runTestCases(String code, String language, String[] inputs, String[] expectedOutputs) {
        Map<String, Object> results = new HashMap<>();

        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            String expectedOutput = expectedOutputs[i];

            // Run the user's code against the current test case
            CompilationResult result = compileAndRun(code + "\nSystem.out.println(\"" + input + "\");", language);

            // Check if the output matches the expected output
            boolean success = result.isSuccess() && expectedOutput.equals(result.getOutput().trim());

            // Add the result for the current test case to the map
            results.put("Test Case " + (i + 1), Map.of(
                    "Input", input,
                    "Expected Output", expectedOutput,
                    "Actual Output", result.getOutput().trim(),
                    "Success", success
            ));
        }

        return results;
    }

    private CompilationResult compileAndRunJava(String code) {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

            // Create a temporary Java source file
            JavaFileObject sourceFile = new JavaSourceFromString("Main", code);

            // Create a file manager with the standard file system
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

            // Compile the source file
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, Arrays.asList("-proc:none"), null, Arrays.asList(sourceFile));
            boolean success = task.call();

            // Capture compilation output and errors
            StringBuilder output = new StringBuilder();
            diagnostics.getDiagnostics().forEach(d -> output.append(d.toString()).append("\n"));

            // Run the compiled code if successful
            if (success) {
                // Get the compiled class bytes
                Map<String, byte[]> classBytes = new HashMap<>();
                for (JavaFileObject classFile : fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(new File("Main.class")))) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try (InputStream is = classFile.openInputStream()) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) > -1) {
                            bos.write(buffer, 0, len);
                        }
                    }
                    classBytes.put(classFile.getName().replace(".class", ""), bos.toByteArray());
                }

                // Create a custom class loader to load the compiled class
                ClassLoader classLoader = new ByteArrayClassLoader(classBytes);
                Class<?> mainClass = classLoader.loadClass("Main");

                // Redirect standard output to capture the program's output
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                System.setOut(new PrintStream(outputStream));

                // Execute the compiled code
                try {
                    mainClass.getDeclaredMethod("main", String[].class).invoke(null, (Object) new String[0]);

                    // Retrieve the captured standard output
                    output.append(outputStream.toString());
                } finally {
                    // Reset the standard output
                    System.setOut(System.out);
                }
            }

            return new CompilationResult(success, output.toString());
        } catch (Exception e) {
            return new CompilationResult(false, "Compilation error: " + e.getMessage());
        }
    }

    private CompilationResult compileAndRunPython(String code) {
        try {
            // Write Python code to a temporary file
            File tempFile = File.createTempFile("temp", ".py");
            try (PrintWriter writer = new PrintWriter(tempFile)) {
                writer.write(code);
            }

            // Build the command to execute Python
            List<String> command = Arrays.asList("python", tempFile.getAbsolutePath());

            // Execute the command using ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Capture the output of the process
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();

            return new CompilationResult(exitCode == 0, output.toString());
        } catch (Exception e) {
            return new CompilationResult(false, "Python compilation and execution error: " + e.getMessage());
        }
    }


    private CompilationResult compileAndRunC(String code) {
        try {
            // Write C code to a temporary file
            File tempFile = File.createTempFile("temp", ".c");
            try (PrintWriter writer = new PrintWriter(tempFile)) {
                writer.write(code);
            }

            // Build the command to compile C code (using GCC as an example)
            List<String> compileCommand = Arrays.asList("gcc", tempFile.getAbsolutePath(), "-o", tempFile.getAbsolutePath() + ".out");

            // Execute the compilation command using ProcessBuilder
            ProcessBuilder compileProcessBuilder = new ProcessBuilder(compileCommand);
            compileProcessBuilder.redirectErrorStream(true);
            Process compileProcess = compileProcessBuilder.start();

            // Capture the compilation output
            StringBuilder compileOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    compileOutput.append(line).append("\n");
                }
            }

            // Wait for the compilation process to complete
            int compileExitCode = compileProcess.waitFor();

            if (compileExitCode == 0) {
                // Build the command to execute the compiled C code
                List<String> executeCommand = Arrays.asList(tempFile.getAbsolutePath() + ".out");

                // Execute the execution command using ProcessBuilder
                ProcessBuilder executeProcessBuilder = new ProcessBuilder(executeCommand);
                executeProcessBuilder.redirectErrorStream(true);
                Process executeProcess = executeProcessBuilder.start();

                // Capture the execution output
                StringBuilder executeOutput = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(executeProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        executeOutput.append(line).append("\n");
                    }
                }

                // Wait for the execution process to complete
                int executeExitCode = executeProcess.waitFor();

                return new CompilationResult(executeExitCode == 0, executeOutput.toString());
            } else {
                // Compilation failed
                return new CompilationResult(false, "C compilation error: " + compileOutput.toString());
            }
        } catch (Exception e) {
            return new CompilationResult(false, "C compilation and execution error: " + e.getMessage());
        }
    }

    static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    static class ByteArrayClassLoader extends ClassLoader {
        private final Map<String, byte[]> classBytes;

        public ByteArrayClassLoader(Map<String, byte[]> classBytes) {
            this.classBytes = classBytes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classBytes.get(name);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.findClass(name);
        }
    }

    public static class CompilationResult {
        private final boolean success;
        private final String output;

        public CompilationResult(boolean success, String output) {
            this.success = success;
            this.output = output;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getOutput() {
            return output;
        }
    }
}
