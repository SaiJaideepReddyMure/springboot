package com.example.demo.controller;

import com.example.demo.model.Code;
import com.example.demo.service.CompilerService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.UserSubService;
import com.example.demo.service.examService;
import com.example.demo.service.studentloadservice;

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
	List<Questions> qList;
	int scount;
	String dreg_no;
    private final CompilerService compilerService;
    private final QuestionService questionService;
    private final  UserSubService userservice;
    private final examService examservice;
    private final studentloadservice studentloadservice;
     
    public CompilerController(CompilerService compilerService, QuestionService questionService,
			UserSubService userservice, examService examservice,studentloadservice studentloadservice) {
		 
		this.compilerService = compilerService;
		this.questionService = questionService;
		this.userservice = userservice;
		this.examservice = examservice;
		this.studentloadservice=studentloadservice;
	}

    

     

    @GetMapping("/")
    public String showCompilerForm(@RequestParam String reg_no,@RequestParam(required = false) Integer exam_id,Model model) {
    	
         //List<Questions> questionList = questionService.getquestions(exam_id);
    	dreg_no = reg_no;
         qList = questionService.getquestions(exam_id);
        int timer = examservice.getTimmer(exam_id);
        System.out.println(timer);
         scount=0;
         System.out.println(reg_no+" "+exam_id);
         while(scount<qList.size())
        {  
        	Questions firstQuestion = qList.isEmpty() ? new Questions() : qList.get(scount);
            String ids = exam_id + "_" + reg_no+"_"+firstQuestion.getId();
            if(!userservice.checking(ids))
            {
            	 break;
            }
            else
            {
            	scount++;
            }
        }
        if(scount==qList.size())
        {
        	return "submited";
        }
       
        Questions firstQuestion = qList.isEmpty() ? new Questions() : qList.get(scount);
        model.addAttribute("reg_no",reg_no);
 
        model.addAttribute("question", firstQuestion);
        model.addAttribute("code", new Code());
       model.addAttribute("timers",timer);    
        
        return "compiler";
    } 

    @PostMapping("/compile")
    public String compileCodeWithTestCases(@RequestParam(required = false) String reg_no,@ModelAttribute Code code, Model model) {
        if (code.getSourceCode() == null) {
        	 
            return "error";
        }
        System.out.println("compiler "+scount);
         
        Questions question = qList.get(scount);
        if (question == null) {
        	 
            return "error";
        }

        String[] openTestCases = question.getOpentestCases();
        String[] openExpectedOutputs = question.getOpentestCasesoutput();

        CompilationResult openCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), openTestCases, openExpectedOutputs);
         
        model.addAttribute("code", code);
        model.addAttribute("reg_no",reg_no);
         model.addAttribute("question", question);
        model.addAttribute("openTestCases", openTestCases);
        model.addAttribute("openExpectedOutputs", openExpectedOutputs);
        model.addAttribute("openCompilationResult", openCompilationResult);

        return "compiler";
    }

    @PostMapping("/tester")
    public String  compileCodeWithClosedTestCases(@ModelAttribute Code code, Model model,@RequestParam String reg_no) 
	    { 
    	if (code.getSourceCode() == null) {
	        return "error";
	    }

     Questions question = qList.stream().filter(q -> q.getId() == code.getQuestionId()).findFirst().orElse(null);

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
    	model.addAttribute("reg_no",reg_no);
    	model.addAttribute("closedTestCases", closedTestCases);
        model.addAttribute("closedExpectedOutputs", closedExpectedOutputs);
        model.addAttribute("closedCompilationResult", closedCompilationResult);
        model.addAttribute("question", question);
        return "compiler";
    	 
    	}
    
    @PostMapping("/submit")
 public String  SubmitCode(@ModelAttribute Code code,@RequestParam String reg_no, Model model) 
 {
    	model.addAttribute("reg_no",reg_no);
    	String reg  =  reg_no;
    	 
    	//List<Questions> questionList = questionService.getAllQuestions();
        Questions question = qList.get(scount);
        int  exam_id = question.getExam_id();
         
        String class_id = examservice.gettingclass_id(exam_id);
    	 
        String[] closedTestCases = question.getClosedtestCases();
        String[] closedExpectedOutputs = question.getClosedtestCasesoutput();
       
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
        String ids = exam_id + "_" + reg+"_"+question.getId();
        userSub.setId(ids);
        userSub.setQid(question.getId());
        userSub.setClass_id(class_id);
        userSub.setReg_no(reg);
        userSub.setScore(score);
        userSub.setCases(result);
        userSub.setExam_id(exam_id);
        
        userservice.saveUserSub(userSub);
        System.out.println("Before "+scount);
        scount++;
        System.out.println("After "+scount);
        if ( scount == qList.size()) {
            studentloadservice.setstatus(exam_id,reg_no); 
            return "submited";
        }
 
         System.out.println(scount);
         return "redirect:/question";
         
 }
    
    
    
    @GetMapping("/question")
    public String showQuestion(Model model) {
    	 
        
        System.out.println("Questions "+scount);
        Questions  question =qList.get(scount);

         
        if (question == null) {
            return "error";  
        }
        model.addAttribute("question", question);
        model.addAttribute("code", new Code());
        model.addAttribute("reg_no",dreg_no);
        return "compiler";
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
}
