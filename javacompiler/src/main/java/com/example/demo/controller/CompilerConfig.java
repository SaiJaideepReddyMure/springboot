package com.example.demo.controller;

 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.CompilerService;
import com.example.demo.service.CompilerServiceImpl;

@Configuration
public class CompilerConfig {

    @Bean
    public CompilerService compilerService() {
        return new CompilerServiceImpl();
    }
}