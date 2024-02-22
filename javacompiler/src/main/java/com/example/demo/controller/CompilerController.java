package com.example.demo.controller;

import com.example.demo.model.Code;
import com.example.demo.service.CompilerService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.UserSubService;
import com.example.demo.service.examService;
import com.example.demo.model.CompilationResult;
 import com.example.demo.model.Questions;
import com.example.demo.model.TestCaseResult;
import com.example.demo.model.usersub;
 

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CompilerController {

    private final CompilerService compilerService;
    private final QuestionService questionService;
    private final  UserSubService userservice;
    private final examService examservice;
     
    public CompilerController(CompilerService compilerService, QuestionService questionService,
			UserSubService userservice, examService examservice) {
		 
		this.compilerService = compilerService;
		this.questionService = questionService;
		this.userservice = userservice;
		this.examservice = examservice;
	}
	 
    

     

    @GetMapping("/")
    public String showCompilerForm(@RequestParam String reg_no,Model model) {
    	
    	if (reg_no != null) {
            // Use reg_no as needed
            // For example, add it to the model
    		
            model.addAttribute("reg_no", reg_no);
            model.addAttribute("check", 0);
            
            
            
        }
    	System.out.println("reg_no : "+reg_no);
        List<Questions> questionList = questionService.getquestions(2);
         

       
        Questions firstQuestion = questionList.isEmpty() ? new Questions() : questionList.get(0);
         
        model.addAttribute("question", firstQuestion);
       model.addAttribute("questionList", questionList);
       model.addAttribute("code", new Code());
          
        
        return "compiler";
    } 

    @PostMapping("/compile")
    public String compileCodeWithTestCases(@ModelAttribute Code code, Model model) {
        if (code.getSourceCode() == null) {
            return "error";
        }

        List<Questions> questionList = questionService.getAllQuestions();
        Questions question = questionList.stream().filter(q -> q.getId() == code.getQuestionId()).findFirst().orElse(null);

        if (question == null) {
            return "error";
        }

 
        
     String[] openTestCases = question.getOpentestCases();
     String[] openExpectedOutputs = question.getOpentestCasesoutput();
 
        CompilationResult openCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), openTestCases, openExpectedOutputs);
         

        model.addAttribute("code", code);
        model.addAttribute("openTestCases", openTestCases);
        model.addAttribute("openExpectedOutputs", openExpectedOutputs);
        model.addAttribute("openCompilationResult", openCompilationResult);
        
 
        model.addAttribute("question", question);

        return "compiler";
    }
    @PostMapping("/tester")
    public String  compileCodeWithClosedTestCases(@ModelAttribute Code code, Model model) 
	    { 
    	if (code.getSourceCode() == null) {
	        return "error";
	    }

    List<Questions> questionList = questionService.getAllQuestions();
    Questions question = questionList.stream().filter(q -> q.getId() == code.getQuestionId()).findFirst().orElse(null);

    if (question == null) {
        return "error";
    }
    String[] openTestCases = question.getOpentestCases();
    String[] openExpectedOutputs = question.getOpentestCasesoutput();
   String[] closedTestCases = question.getClosedtestCases();
   String[] closedExpectedOutputs = question.getClosedtestCasesoutput();
    CompilationResult openCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), openTestCases, openExpectedOutputs);

 
          CompilationResult closedCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), closedTestCases, closedExpectedOutputs);
          model.addAttribute("openTestCases", openTestCases);
          model.addAttribute("openExpectedOutputs", openExpectedOutputs);
          model.addAttribute("openCompilationResult", openCompilationResult);
    	model.addAttribute("code", code);
  
    	model.addAttribute("closedTestCases", closedTestCases);
        model.addAttribute("closedExpectedOutputs", closedExpectedOutputs);
        model.addAttribute("closedCompilationResult", closedCompilationResult);
        model.addAttribute("question", question);
        return "compiler";
    	 
    	}
    
    @PostMapping("/submit")
 public String  SubmitCode(@ModelAttribute Code code, Model model) 
 {
    	
    	String reg_no = "20BCE2287";
    	 
    	List<Questions> questionList = questionService.getAllQuestions();
        Questions question = questionList.stream().filter(q -> q.getId() == code.getQuestionId()).findFirst().orElse(null);
        int  exam_id = question.getExam_id();
        //String class_id = examservice.getclass_id(exam_id); 
        String class_id = examservice.gettingclass_id(exam_id);
    	//String[] openTestCases = question.getOpentestCases();
        //String[] openExpectedOutputs = question.getOpentestCasesoutput();
        String[] closedTestCases = question.getClosedtestCases();
        String[] closedExpectedOutputs = question.getClosedtestCasesoutput();
       // CompilationResult openCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), openTestCases, openExpectedOutputs);
        CompilationResult closedCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), closedTestCases, closedExpectedOutputs);
 
        List<TestCaseResult> y= closedCompilationResult.getOutputResults();
        boolean[] y1 = new boolean[y.size()];
        for(int i =0;i<y.size();i++)
        {
        	y1[i]= y.get(i).isTestCasePassed();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (boolean b : y1) {
            stringBuilder.append(b ? "1" : "0");
        }

        String result = stringBuilder.toString();
        int score = countTrueValues(y1);
        usersub userSub = new usersub();
        String ids = class_id + "_" + reg_no+"_"+question.getId();
        userSub.setId(ids);
        userSub.setQid(question.getId());
        userSub.setClass_id(class_id);
        userSub.setReg_no(reg_no);
        userSub.setScore(score);
        userSub.setCases(result);
        userSub.setExam_id(exam_id);

        userservice.saveUserSub(userSub); 
        if (question.getId() == questionList.size()) {
             
            return "submited";
        }
//        int nextQuestionId = question.getId() + 1;
//         return "redirect:/question/" + nextQuestionId;
        int currentQuestionId = code.getQuestionId();
        int nextQuestionId = currentQuestionId + 1;

        // Redirect the user to the next question
        return "redirect:/question/" + nextQuestionId;
         
 }
    private static int countTrueValues(boolean[] array) {
        int count = 0;
        for (boolean value : array) {
            if (value) {
                count++;
            }
        }
        return count;
    }
    @GetMapping("/question/{id}")
    public String showQuestion(@PathVariable("id") int id, Model model) {
    	 
        List<Questions> questionList = questionService.getquestions(2);
        
        if (id < 1 || id > questionList.size()) {
            return "error"; // Return an error page or handle the out-of-bounds case appropriately
        }
        Questions question = questionList.get(id-1);
        if (question == null) {
            return "error";  
        }
        model.addAttribute("question", question);
        model.addAttribute("code", new Code());
        return "compiler";
    }

 
    

}
