package com.example.demo.controller;

 

 import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.service.CompilerService;

@RestController
@RequestMapping("/open")
public class CompilerController {

    private final CompilerService compilerService;

    @Autowired
    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @PostMapping("/compile")
    @ResponseBody
    public Map<String, Object> compileCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String language = request.get("language");

        CompilerService.CompilationResult result = compilerService.compileAndRun(code, language);

        return Map.of(
                "success", result.isSuccess(),
                "output", result.getOutput()
        );
    }
    @PostMapping("/runTestCases")
    @ResponseBody
    public Map<String, Object> runTestCases(@RequestBody Map<String, Object> request) {
        String code = (String) request.get("code");
        String language = (String) request.get("language");
        List<String> inputs = (List<String>) request.get("inputs");
        List<String> expectedOutputs = (List<String>) request.get("expectedOutputs");

        Map<String, Object> results = compilerService.runTestCases(code, language, inputs.toArray(new String[0]), expectedOutputs.toArray(new String[0]));

        return Map.of(
                "results", results
        );
    }

}