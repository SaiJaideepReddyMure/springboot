package com.example.demo.controller;

import java.util.List;

 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Code;
import com.example.demo.model.CompilationResult;
import com.example.demo.model.Questions;
import com.example.demo.model.TestCaseResult;
import com.example.demo.model.usersub;
import com.example.demo.service.CompilerService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.UserSubService;
import com.example.demo.service.examService;
import com.example.demo.service.studentloadservice;

import jakarta.servlet.http.HttpSession;
 

 
@Controller
public class CompilerController {
	 
	int scount;
	HttpSession session;
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
    // public String showCompilerForm(@RequestParam String reg_no,@RequestParam(required = false) Integer exam_id,Model model) {
    public String showCompilerForm(HttpSession session1, Model model) {	
         //List<Questions> questionList = questionService.getquestions(exam_id);
    	//dreg_no = reg_no;
    	session=session1;
    	 String reg_no = (String) session.getAttribute("reg_no");
    	    Integer exam_id = (Integer) session.getAttribute("exam_id");
            
    	    if (reg_no == null || exam_id == null) {
    	        // Handle the case where session attributes are not found
    	        return "redirect:/loginpage";
    	    }
    	List<Questions> qList = questionService.getquestions(exam_id);
       // int timer = examservice.getTimmer(exam_id);
    	int timer = studentloadservice.loading_timer(reg_no,exam_id);
        //System.out.println(timer);
         scount=0;
       //  System.out.println(reg_no+" "+exam_id);
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
        String lang =  examservice.getting_lang(exam_id);
        model.addAttribute("reg_no",reg_no);
        model.addAttribute("scount",scount);
        model.addAttribute("exam_id", exam_id);
        model.addAttribute("question", firstQuestion);
        model.addAttribute("code", new Code());
        model.addAttribute("lang", lang);
        model.addAttribute("timers",timer);    
        
        return "compiler";
    } 

    @PostMapping("/compile")
    public String compileCodeWithTestCases(@RequestParam(required = false) String reg_no,@ModelAttribute Code code, Model model,@RequestParam  Integer exam_id) {
        if (code.getSourceCode() == null) {
        	 
            return "error";
        }
        
        List<Questions> qList = questionService.getquestions(exam_id);
        Questions question = qList.get(scount);
        if (question == null) {
        	 
            return "error";
        }

        String[] openTestCases = question.getOpentestCases();
        String[] openExpectedOutputs = question.getOpentestCasesoutput();
        CompilationResult openCompilationResult = null;
        if("Java".equals(examservice.getting_lang(exam_id)))
        {
              openCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), openTestCases, openExpectedOutputs);
         }
        else if("C".equals(examservice.getting_lang(exam_id)))
        {
             openCompilationResult = compilerService.compileAndRunWithTestCasesForC(code.getSourceCode(), openTestCases, openExpectedOutputs);
 
        }
        else if("Python".equals(examservice.getting_lang(exam_id)))
        {
 
             openCompilationResult = compilerService.compileAndRunWithTestCasesForPython(code.getSourceCode(), openTestCases, openExpectedOutputs);
 
        }

        //CompilationResult openCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), openTestCases, openExpectedOutputs);
         
        model.addAttribute("code", code);
        model.addAttribute("reg_no",reg_no);
         model.addAttribute("question", question);
        model.addAttribute("openTestCases", openTestCases);
        model.addAttribute("openExpectedOutputs", openExpectedOutputs);
         
		model.addAttribute("openCompilationResult", openCompilationResult);

        return "compiler";
    }

    @PostMapping("/tester")
    public String  compileCodeWithClosedTestCases(@ModelAttribute Code code, Model model,@RequestParam int exam_id,@RequestParam(required = false) String reg_no) 
	    { 
    	if (code.getSourceCode() == null) {
	        return "error";
	    }

     Questions question = questionService.getquestions(exam_id).stream().filter(q -> q.getId() == code.getQuestionId()).findFirst().orElse(null);

    if (question == null) {
        return "error";
    }
    String[] openTestCases = question.getOpentestCases();
    String[] openExpectedOutputs = question.getOpentestCasesoutput();
   String[] closedTestCases = question.getClosedtestCases();
   String[] closedExpectedOutputs = question.getClosedtestCasesoutput();
   CompilationResult openCompilationResult = null;
   CompilationResult closedCompilationResult = null;
    if("Java".equals(examservice.getting_lang(exam_id))) {
	    openCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), openTestCases, openExpectedOutputs);
        closedCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), closedTestCases, closedExpectedOutputs);

 	} else if("C".equals(examservice.getting_lang(exam_id))) {
	    openCompilationResult = compilerService.compileAndRunWithTestCasesForC(code.getSourceCode(), openTestCases, openExpectedOutputs);
	    closedCompilationResult = compilerService.compileAndRunWithTestCasesForC(code.getSourceCode(), closedTestCases, closedExpectedOutputs);

 	} else {
	    openCompilationResult = compilerService.compileAndRunWithTestCasesForPython(code.getSourceCode(), openTestCases, openExpectedOutputs);
	    closedCompilationResult = compilerService.compileAndRunWithTestCasesForPython(code.getSourceCode(), closedTestCases, closedExpectedOutputs);

	}

    //CompilationResult openCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), openTestCases, openExpectedOutputs);

//         System.out.println(closedCompilationResult.getOutputResults().get(0).getExpectedOutput());
//         System.out.println(closedCompilationResult.getOutputResults().get(0).getActualOutput());

         // CompilationResult closedCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), closedTestCases, closedExpectedOutputs);
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
    public String SubmitCode( @ModelAttribute Code code, @RequestParam String reg_no, Model model,@RequestParam("scount") int scount,
    		@RequestParam("exam_id") int exam_id
            ) {
    	model.addAttribute("reg_no", reg_no);
        String reg = reg_no;
         
        Questions question = questionService.getquestions(exam_id).get(scount);
         

        String class_id = examservice.gettingclass_id(exam_id);
        
        

        String[] closedTestCases = question.getClosedtestCases();
        String[] closedExpectedOutputs = question.getClosedtestCasesoutput();
        
        
        CompilationResult closedCompilationResult = null;
        if("Java".equals(examservice.getting_lang(exam_id)))
        {
                closedCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), closedTestCases, closedExpectedOutputs);

        }
        else if("C".equals(examservice.getting_lang(exam_id)))
        {
                closedCompilationResult = compilerService.compileAndRunWithTestCasesForC(code.getSourceCode(), closedTestCases, closedExpectedOutputs);

        }
        else
        {
               closedCompilationResult = compilerService.compileAndRunWithTestCasesForPython(code.getSourceCode(), closedTestCases, closedExpectedOutputs);

        }

        //CompilationResult closedCompilationResult = compilerService.compileAndRunWithTestCases(code.getSourceCode(), closedTestCases, closedExpectedOutputs);
         
        List<TestCaseResult> y = closedCompilationResult.getOutputResults();
 
        
        boolean[] y1 = new boolean[y.size()];
        for (int i = 0; i < y.size(); i++) {
            y1[i] = y.get(i).isTestCasePassed();
                     
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (boolean b : y1) {
            stringBuilder.append(b ? "1" : "0");
        }

        String result = stringBuilder.toString();
        int score = countTrueValues(y1);
        usersub userSub = new usersub();
        String ids = exam_id + "_" + reg + "_" + question.getId();
       
        userSub.setId(ids);
        userSub.setQid(question.getId());
        userSub.setClass_id(class_id);
        userSub.setReg_no(reg);
        
        userSub.setScore(score);
        userSub.setCases(result);
        
        userSub.setExam_id(exam_id);

        userservice.saveUserSub(userSub);
         
        scount++;
        model.addAttribute("scount", scount);
        List<Questions> qList = questionService.getquestions(exam_id);
        if (scount == qList.size()) {
            studentloadservice.setstatus(exam_id, reg_no);
            session.invalidate();
             return "redirect:/completed"; 
        }

        return "redirect:/?exam_id=" + exam_id + "&reg_no=" + reg_no + "&scount=" + scount;    }

    
    @GetMapping("/question")
    public String showQuestion(Model model,@RequestParam int exam_id,@RequestParam String reg_no,@RequestParam int scount) {
    	 
        
        
        Questions  question =questionService.getquestions(exam_id).get(scount);

         
        if (question == null) {
            return "error";  
        }
        model.addAttribute("question", question);
        model.addAttribute("code", new Code());
         
        return "compiler";
    }
    @GetMapping("/completed")
    public static String submitted()
    {
    	return "submited";
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
    @PostMapping("/autosubmit")
    private String autosubmit(@RequestParam String reg_no, 
                              @RequestParam int exam_id, 
                              @RequestParam int scount,
                              Model model) {
 
        List<Questions> qList = questionService.getquestions(exam_id);
        
                 for (int i = scount; i < qList.size(); i++) {
            Questions question = qList.get(i);
            String ids = exam_id + "_" + reg_no + "_" + question.getId();
            
            
            if (!userservice.checking(ids)) {
                
                Code code = new Code();
                if(code.getSourceCode()==null)
                {
                code.setSourceCode("");  
                }
                 
                SubmitCode(code, reg_no, model,i,exam_id); // Call the corrected submitCode method
            }
        }
        
         
        return "submited";
    }
    @GetMapping("/kl")
    public String triggerKLController(@RequestParam String reg_no, @RequestParam String timer, @RequestParam String exam_id) {
         
        studentloadservice.updatetimer(reg_no,timer,exam_id);
        
         return "redirect:/";
     }

}
