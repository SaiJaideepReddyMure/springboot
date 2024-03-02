package com.example.demo.controller;

import java.time.LocalDateTime;
 

 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.examService;
import com.example.demo.service.studentloadservice;

 

@Controller
public class LoginController {
	@Autowired
	private studentloadservice slds;
	@Autowired
	private examService es;
	public LoginController(studentloadservice slds, examService es) {
		super();
		this.slds = slds;
		this.es = es;
	}
	@GetMapping("/loginpage")
    public String displayLoginPage(Model model) {
		model.addAttribute("error", "");
        return "loginpage";
    }
    @PostMapping("/login")
    public String login(@RequestParam String reg_no, @RequestParam String password,Model model,RedirectAttributes redirectAttributes) {
    	  LocalDateTime currentDateTime = LocalDateTime.now();
          
          if ((reg_no.equals("20BCE7287") ||  reg_no.equals("20BCE7174") )&& password.equals("password")) {
              List<Integer> exam_ids = slds.setingExamIds(reg_no);
                

              for (Integer exam_id : exam_ids) {
                  LocalDateTime startTime = es.getTimmeract(exam_id);
                  LocalDateTime endTime = startTime.plusMinutes(1000000000); 
                  
                  if (currentDateTime.isAfter(startTime) && currentDateTime.isBefore(endTime)) {
                	  
                	  boolean submitted = slds.checkingstatus(exam_id,reg_no);
                	  if(!submitted)
                	  {   
                		                		   
                		    return "redirect:/?reg_no=" + reg_no+ "&exam_id=" + exam_id;
                	  }
                      
                      
                  }
            }

              
              model.addAttribute("error", "You are not registered for any exam or exam is not available at the moment.");
 
              return "loginpage";
          } else {
               
        	 

        	  model.addAttribute("error", "Invalid credentials. Please try again.");
              return "loginpage";
          }
    }
	
    
}
