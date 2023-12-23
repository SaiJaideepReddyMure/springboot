package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Info;
import com.example.demo.service.crudservice;

@RestController
@CrossOrigin
public class Crudcontroller

{
	@Autowired
	private crudservice crudservice;
	
	@GetMapping("/index")
public ResponseEntity<List<Info>> load()
{
		  return crudservice.display();
          
}
	@GetMapping("/display")
	public ModelAndView display() {
		ModelAndView mv = new ModelAndView("display");
        List<Info> infos = crudservice.dis();
        mv.addObject("infos", infos);
        
        
        return mv;
    }
 
 
	@PostMapping("/add")
	public String addInfo(@ModelAttribute Info info) {
	    // Save the info object using your service
	    crudservice.add(info);
	    return "redirect:/display";
	}
	@GetMapping("/addinginfo")
    public ModelAndView showAddingInfoForm() {
		ModelAndView mv = new ModelAndView("addinginfo");
        return  mv;
    }
 
	@GetMapping("/updateinfo/{id}")
    public  ModelAndView showUpdateInfoPage(@PathVariable Integer id) {
        // Create an Info object with the provided parameters
        Optional<Info> info =  crudservice.displayone(id);
         ModelAndView mv = new ModelAndView("updateinfo");

        // Add the info object to the model to pre-fill the form
         mv.addObject("info", info);

        return mv; // Thymeleaf template name
    }
 
	@PostMapping("/del/{id}")
	public ModelAndView delete(@PathVariable("id") Integer id) {
	    crudservice.del(id);
	    return new ModelAndView("redirect:/display");
	}
	@PostMapping("/update/{id}")
	public  ModelAndView updateinfo(@PathVariable("id") Integer id,Info info)
	{  crudservice.update(id,info);
	ModelAndView mv = new ModelAndView("redirect:/display");
	    
		return  mv;
	}
}
