package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@RequestMapping("home")
	 
	
//	public String home(HttpServletRequest req) { // using http request
//		
//		String name = req.getParameter("name");
//		System.out.println("hi");
//		req.setAttribute("name",name);
//		return "home";
//		
//	}
//	public String home(HttpServletRequest req) // using sessions
//	{
//		HttpSession session = req.getSession();
//		String name = req.getParameter("name");
//		System.out.println("hi");
//		session.setAttribute("name",name);
//		return "home";
//		
//	}
//	public String home(@RequestParam("name")String Myname,HttpSession session)
//	
//	{
//		System.out.println("hi"+Myname);
//		session.setAttribute("name", Myname);// works if the variable should be name only like ?name = jaideep not ?name3=jaideep wont work
//		 return "home";                    // same we need an annotation @RequestParam("")
//	}
//	public ModelAndView home(@RequestParam("name")String Myname)// for single parameter 
//	
//	{
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("name",Myname);
//		mv.setViewName("home");
// 		return mv;
//	}
public ModelAndView home(Alien a)// for taking object itself using rather than variable
	
	{
		ModelAndView mv = new ModelAndView();
		mv.addObject("obj",a);
		mv.setViewName("home");
 		return mv;
	}

}
