package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.service.CodeService;
import com.example.demo.service.CodeService.TestCase;
import com.example.demo.service.CodeService.TestResult;

@RestController
@RequestMapping("/code")
public class CodeController {
	@Autowired
    private final CodeService codeService;

    
    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("code", "");
        // Add any additional model attributes if needed
        return modelAndView;
    }
    @PostMapping("/compile")
    public ModelAndView compileAndRunCode(@ModelAttribute("code") String code) {
        ModelAndView modelAndView = new ModelAndView("index");
        
        // Add the code to the modelAndView so that it remains visible in the view
        modelAndView.addObject("code", code); 
        

        try {
            // Set output to an empty string initially
            modelAndView.addObject("output", "");

            // Run the code
            String output = codeService.compileAndRunCode(code);
            
            // Update the output in the modelAndView
            modelAndView.addObject("output", output);
        } catch (Exception e) {
            modelAndView.addObject("output", "Error: " + e.getMessage());
        }

        return modelAndView;
    }

     
}